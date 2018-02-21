/*
 * Copyright @ 2015 Atlassian Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jitsi.impl.neomedia.protocol;

import java.io.*;
import java.util.*;

import javax.media.*;
import javax.media.format.*;
import javax.media.protocol.*;

import net.sf.fmj.media.util.*;

import org.jitsi.impl.neomedia.jmfext.media.renderer.*;
import org.jitsi.util.*;

/**
 * Implements <tt>PushBufferDataSource</tt> for a specific
 * <tt>PullBufferDataSource</tt>.
 *
 * @author Lyubomir Marinov
 */
public class PushBufferDataSourceAdapter
    extends PushBufferDataSourceDelegate<PullBufferDataSource>
{

    /**
     * Implements <tt>PushBufferStream</tt> for a specific
     * <tt>PullBufferStream</tt>.
     */
    private static class PushBufferStreamAdapter
        implements PushBufferStream
    {

        /**
         * The <tt>Buffer</tt> which contains the media data read by this
         * instance from {@link #stream} and to be returned by this
         * implementation of {@link PushBufferStream#read(Buffer)} by copying.
         */
        private final Buffer buffer = new Buffer();

        /**
         * The indicator which determines whether {@link #buffer} contains media
         * data read by this instance from {@link #stream} and not returned by
         * this implementation of {@link PushBufferStream#read(Buffer)} yet.
         */
        private boolean bufferIsWritten = false;

        /**
         * The indicator which determined whether {@link  #start()} has been
         * called without a subsequent call to {@link #stop()}.
         */
        private boolean started = false;

        /**
         * The <tt>PullBufferStream</tt> to which this instance provides
         * <tt>PushBufferStream</tt> capabilities.
         */
        public final PullBufferStream stream;

        /**
         * The <tt>IOException</tt>, if any, which has been thrown by the last
         * call to {@link PullBufferStream#read(Buffer)} on {@link #stream} and
         * which still hasn't been rethrown by this implementation of
         * {@link PushBufferStream#read(Buffer)}.
         */
        private IOException streamReadException;

        /**
         * The <tt>Thread</tt> which currently reads media data from
         * {@link #stream} into {@link #buffer}.
         */
        private Thread streamReadThread;

        /**
         * The <tt>Object</tt> which synchronizes the access to
         * {@link #streamReadThread}-related members.
         */
        private final Object streamReadThreadSyncRoot = new Object();

        /**
         * The <tt>BufferTransferHandler</tt> through which this
         * <tt>PushBufferStream</tt> notifies its user that media data is
         * available for reading.
         */
        private BufferTransferHandler transferHandler;

        /**
         * Initializes a new <tt>PushBufferStreamAdapter</tt> instance which is
         * to implement <tt>PushBufferStream</tt> for a specific
         * <tt>PullBufferStream</tt>.
         *
         * @param stream the <tt>PullBufferStream</tt> the new instance is to
         * implement <tt>PushBufferStream</tt> for
         */
        public PushBufferStreamAdapter(PullBufferStream stream)
        {
            if (stream == null)
                throw new NullPointerException("stream");

            this.stream = stream;
        }

        /**
         * Disposes of this <tt>PushBufferStreamAdapter</tt>. Afterwards, this
         * instance is not guaranteed to be operation and considered to be
         * available for garbage collection.
         */
        void close()
        {
            stop();
        }

        /**
         * Implements {@link SourceStream#endOfStream()}. Delegates to the
         * wrapped <tt>PullBufferStream</tt>.
         *
         * @return <tt>true</tt> if the wrapped <tt>PullBufferStream</tt> has
         * reached the end of the media data; otherwise, <tt>false</tt>
         */
        @Override
        public boolean endOfStream()
        {
            return stream.endOfStream();
        }

        /**
         * Implements {@link SourceStream#getContentDescriptor()}. Delegates to
         * the wrapped <tt>PullBufferStream</tt>.
         *
         * @return the <tt>ContentDescriptor</tt> of the wrapped
         * <tt>PullBufferStream</tt> which describes the type of the media data
         * it gives access to
         */
        @Override
        public ContentDescriptor getContentDescriptor()
        {
            return stream.getContentDescriptor();
        }

        /**
         * Implements {@link SourceStream#getContentLength()}. Delegates to the
         * wrapped <tt>PullBufferStream</tt>.
         *
         * @return the length of the content the wrapped
         * <tt>PullBufferStream</tt> gives access to
         */
        @Override
        public long getContentLength()
        {
            return stream.getContentLength();
        }

        /**
         * Implements {@link javax.media.Controls#getControl(String)}. Delegates
         * to the wrapped <tt>PullBufferStream</tt>.
         *
         * @param controlType a <tt>String</tt> value which specifies the type
         * of the control of the wrapped <tt>PullBufferStream</tt> to be
         * retrieved
         * @return an <tt>Object</tt> which represents the control of the
         * wrapped <tt>PushBufferStream</tt> of the requested type if the
         * wrapped <tt>PushBufferStream</tt> has such a control; <tt>null</tt>
         * if the wrapped <tt>PushBufferStream</tt> does not have a control of
         * the specified type
         */
        @Override
        public Object getControl(String controlType)
        {
            return stream.getControl(controlType);
        }

        /**
         * Implements {@link javax.media.Controls#getControls()}. Delegates to
         * the wrapped <tt>PushBufferStream</tt>.
         *
         * @return an array of <tt>Object</tt>s which represent the controls
         * available for the wrapped <tt>PushBufferStream</tt>
         */
        @Override
        public Object[] getControls()
        {
            return stream.getControls();
        }

        /**
         * Implements {@link PushBufferStream#getFormat()}. Delegates to the
         * wrapped <tt>PullBufferStream</tt>.
         *
         * @return the <tt>Format</tt> of the wrapped <tt>PullBufferStream</tt>
         */
        @Override
        public Format getFormat()
        {
            return stream.getFormat();
        }

        /**
         * Implements {@link PushBufferStream#read(Buffer)}.
         *
         * @param buffer a <tt>Buffer</tt> in which media data is to be written
         * by this <tt>PushBufferDataSource</tt>
         * @throws IOException if anything wrong happens while reading media
         * data from this <tt>PushBufferDataSource</tt> into the specified
         * <tt>buffer</tt>
         */
        @Override
        public void read(Buffer buffer)
            throws IOException
        {
            synchronized (this.buffer)
            {
                /*
                 * If stream has throw an exception during its last read,
                 * rethrow it as an exception of this stream.
                 */
                if (streamReadException != null)
                {
                    IOException ie = new IOException();

                    ie.initCause(streamReadException);
                    streamReadException = null;
                    throw ie;
                }
                else if (bufferIsWritten)
                {
                    buffer.copy(this.buffer);
                    bufferIsWritten = false;
                }
                else
                    buffer.setLength(0);
            }
        }

        /**
         * Executes an iteration of {@link #streamReadThread} i.e. reads media
         * data from {@link #stream} into {@link #buffer} and invokes
         * {@link BufferTransferHandler#transferData(PushBufferStream)} on
         * {@link #transferHandler} if any.
         */
        private void runInStreamReadThread()
        {
            boolean bufferIsWritten;
            boolean yield;

            synchronized (buffer)
            {
                try
                {
                    stream.read(buffer);
                    this.bufferIsWritten = !buffer.isDiscard();
                    streamReadException = null;
                }
                catch (IOException ie)
                {
                    this.bufferIsWritten = false;
                    streamReadException = ie;
                }
                bufferIsWritten = this.bufferIsWritten;
                /*
                 * If an exception has been thrown by the stream's read method,
                 * it may be better to give the stream's underlying
                 * implementation (e.g. PortAudio) a little time to possibly get
                 * its act together.
                 */
                yield = (!bufferIsWritten && (streamReadException != null));
            }

            if (bufferIsWritten)
            {
                BufferTransferHandler transferHandler = this.transferHandler;

                if (transferHandler != null)
                    transferHandler.transferData(this);
            }
            else if (yield)
                Thread.yield();
        }

        /**
         * Implements
         * {@link PushBufferStream#setTransferHandler(BufferTransferHandler)}.
         * Sets the means through which this <tt>PushBufferStream</tt> is to
         * notify its user that media data is available for reading.
         *
         * @param transferHandler the <tt>BufferTransferHandler</tt> through
         * which <tt>PushBufferStream</tt> is to notify its user that media data
         * is available for reading
         */
        @Override
        public void setTransferHandler(BufferTransferHandler transferHandler)
        {
            if (this.transferHandler != transferHandler)
                this.transferHandler = transferHandler;
        }

        /**
         * Starts the reading of media data of this
         * <tt>PushBufferStreamAdapter</tt> from the wrapped
         * <tt>PullBufferStream</tt>.
         */
        void start()
        {
            synchronized (streamReadThreadSyncRoot)
            {
                PushBufferStreamAdapter.this.started = true;

                if (streamReadThread == null)
                {
                    streamReadThread
                        = new Thread(getClass().getName() + ".streamReadThread")
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    setStreamReadThreadPriority(stream);

                                    while (true)
                                    {
                                        synchronized (streamReadThreadSyncRoot)
                                        {
                                            if (!PushBufferStreamAdapter.this.started)
                                                break;
                                            if (streamReadThread
                                                    != Thread.currentThread())
                                                break;
                                        }
                                        runInStreamReadThread();
                                    }
                                }
                                finally
                                {
                                    synchronized (streamReadThreadSyncRoot)
                                    {
                                        if (streamReadThread
                                                == Thread.currentThread())
                                        {
                                            streamReadThread = null;
                                            streamReadThreadSyncRoot
                                                .notifyAll();
                                        }
                                    }
                                }
                            }
                        };
                    streamReadThread.setDaemon(true);
                    streamReadThread.start();
                }
            }
        }

        /**
         * Stops the reading of media data of this
         * <tt>PushBufferStreamAdapter</tt> from the wrapped
         * <tt>PullBufferStream</tt>.
         */
        void stop()
        {
            synchronized (streamReadThreadSyncRoot)
            {
                started = false;
                if (STRICT_STOP)
                {
                    boolean interrupted = false;

                    while (streamReadThread != null)
                        try
                        {
                            streamReadThreadSyncRoot.wait();
                        }
                        catch (InterruptedException iex)
                        {
                            logger
                                .info(
                                    getClass().getSimpleName()
                                        + " interrupted while waiting for"
                                        + " PullBufferStream read thread"
                                        + " to stop.",
                                    iex);
                            interrupted = true;
                        }
                    if (interrupted)
                        Thread.currentThread().interrupt();
                }
                else
                    streamReadThread = null;
            }
        }
    }

    /**
     * The <tt>Logger</tt> used by the <tt>PushBufferDataSourceAdapter</tt>
     * class and its instances for logging output.
     */
    private static final Logger logger
        = Logger.getLogger(PushBufferDataSourceAdapter.class);

    /**
     * The indicator which determines whether the
     * <tt>PushBufferStreamAdapater</tt> instances should wait for their
     * {@link PushBufferStreamAdapter#streamReadThread}s to exit before their
     * {@link PushBufferStreamAdapter#stop()} returns.
     */
    private static final boolean STRICT_STOP = false;

    /**
     * The indicator which determines whether {@link #start()} has been called
     * on this <tt>DataSource</tt> without a subsequent call to {@link #stop()}.
     */
    private boolean started = false;

    /**
     * The <tt>PushBufferStream</tt>s through which this
     * <tt>PushBufferDataSource</tt> gives access to its media data.
     */
    private final List<PushBufferStreamAdapter> streams
        = new ArrayList<PushBufferStreamAdapter>();

    /**
     * Initializes a new <tt>PushBufferDataSourceAdapter</tt> which is to
     * implement <tt>PushBufferDataSource</tt> capabilities for a specific
     * <tt>PullBufferDataSource</tt>.
     *
     * @param dataSource the <tt>PullBufferDataSource</tt> the new instance is
     * to implement <tt>PushBufferDataSource</tt> capabilities for
     */
    public PushBufferDataSourceAdapter(PullBufferDataSource dataSource)
    {
        super(dataSource);
    }

    /**
     * Implements {@link DataSource#disconnect()}. Disposes of the
     * <tt>PushBufferStreamAdapter</tt>s which wrap the
     * <tt>PullBufferStream</tt>s of the <tt>PullBufferDataSource</tt> wrapped
     * by this instance.
     */
    @Override
    public void disconnect()
    {
        synchronized (streams)
        {
            Iterator<PushBufferStreamAdapter> streamIter = streams.iterator();

            while (streamIter.hasNext())
            {
                PushBufferStreamAdapter stream = streamIter.next();

                streamIter.remove();
                stream.close();
            }
        }

        super.disconnect();
    }

    /**
     * Implements {@link PushBufferDataSource#getStreams()}. Gets the
     * <tt>PushBufferStream</tt>s through which this
     * <tt>PushBufferDataSource</tt> gives access to its media data.
     *
     * @return an array of <tt>PushBufferStream</tt>s through which this
     * <tt>PushBufferDataSource</tt> gives access to its media data
     */
    @Override
    public PushBufferStream[] getStreams()
    {
        synchronized (streams)
        {
            PullBufferStream[] dataSourceStreams = dataSource.getStreams();
            int dataSourceStreamCount;

            /*
             * I don't know whether dataSource returns a copy of its internal
             * storage so I'm not sure if it's safe to modify dataSourceStreams.
             */
            if (dataSourceStreams != null)
            {
                dataSourceStreams = dataSourceStreams.clone();
                dataSourceStreamCount = dataSourceStreams.length;
            }
            else
                dataSourceStreamCount = 0;

            /*
             * Dispose of the PushBufferStreamAdapters which adapt
             * PullBufferStreams which are no longer returned by dataSource.
             */
            Iterator<PushBufferStreamAdapter> streamIter = streams.iterator();

            while (streamIter.hasNext())
            {
                PushBufferStreamAdapter streamAdapter = streamIter.next();
                PullBufferStream stream = streamAdapter.stream;
                boolean removeStream = true;

                for (int dataSourceStreamIndex = 0;
                        dataSourceStreamIndex < dataSourceStreamCount;
                        dataSourceStreamIndex++)
                    if (stream == dataSourceStreams[dataSourceStreamIndex])
                    {
                        removeStream = false;
                        dataSourceStreams[dataSourceStreamIndex] = null;
                        break;
                    }
                if (removeStream)
                {
                    streamIter.remove();
                    streamAdapter.close();
                }
            }

            /*
             * Create PushBufferStreamAdapters for the PullBufferStreams
             * returned by dataSource which are not adapted yet.
             */
            for (int dataSourceStreamIndex = 0;
                    dataSourceStreamIndex < dataSourceStreamCount;
                    dataSourceStreamIndex++)
            {
                PullBufferStream dataSourceStream
                    = dataSourceStreams[dataSourceStreamIndex];

                if (dataSourceStream != null)
                {
                    PushBufferStreamAdapter stream
                        = new PushBufferStreamAdapter(dataSourceStream);

                    streams.add(stream);
                    if (started)
                        stream.start();
                }
            }

            return streams.toArray(EMPTY_STREAMS);
        }
    }

    /**
     * Sets the priority of the <tt>streamReadThread</tt> of a
     * <tt>PushBufferStreamAdapter</tt> that adapts a specific
     * <tt>PullBufferStream</tt> in accord with the <tt>Format</tt> of the media
     * data.
     *
     * @param stream the <tt>PullBufferStream</tt> adapted by a
     * <tt>PushBufferStreamAdapter</tt> that is to have the priority of its
     * <tt>streamReadThread</tt> set
     */
    private static void setStreamReadThreadPriority(PullBufferStream stream)
    {
        try
        {
            Format format = stream.getFormat();
            int threadPriority;

            if (format instanceof AudioFormat)
                threadPriority = MediaThread.getAudioPriority();
            else if (format instanceof VideoFormat)
                threadPriority = MediaThread.getVideoPriority();
            else
                return;

            AbstractRenderer.useThreadPriority(threadPriority);
        }
        catch (Throwable t)
        {
            if (t instanceof InterruptedException)
                Thread.currentThread().interrupt();
            else if (t instanceof ThreadDeath)
                throw (ThreadDeath) t;

            logger.warn("Failed to set the priority of streamReadThread");
        }
    }

    /**
     * Implements {@link DataSource#start()}. Starts the wrapped
     * <tt>PullBufferDataSource</tt> and the pushing from the
     * <tt>PushBufferStreamAdapter</tt>s which wrap the
     * <tt>PullBufferStream</tt>s of the <tt>PullBufferDataSource</tt> wrapped
     * by this instance.
     *
     * @throws IOException if anything wrong happens while starting the wrapped
     * <tt>PullBufferDataSource</tt> or the pushing from the
     * <tt>PushBufferStreamAdapter</tt>s which wrap the
     * <tt>PullBufferStream</tt>s of the <tt>PullBufferDataSource</tt> wrapped
     * by this instance
     */
    @Override
    public void start()
        throws IOException
    {
        super.start();

        synchronized (streams)
        {
            started = true;

            for (PushBufferStreamAdapter stream : streams)
                stream.start();
        }
    }

    /**
     * Implements {@link DataSource#start()}. Stops the wrapped
     * <tt>PullBufferDataSource</tt> and the pushing from the
     * <tt>PushBufferStreamAdapter</tt>s which wrap the
     * <tt>PullBufferStream</tt>s of the <tt>PullBufferDataSource</tt> wrapped
     * by this instance.
     *
     * @throws IOException if anything wrong happens while stopping the wrapped
     * <tt>PullBufferDataSource</tt> or the pushing from the
     * <tt>PushBufferStreamAdapter</tt>s which wrap the
     * <tt>PullBufferStream</tt>s of the <tt>PullBufferDataSource</tt> wrapped
     * by this instance
     */
    @Override
    public void stop()
        throws IOException
    {
        synchronized (streams)
        {
            started = false;

            for (PushBufferStreamAdapter stream : streams)
                stream.stop();
        }

        super.stop();
    }
}
