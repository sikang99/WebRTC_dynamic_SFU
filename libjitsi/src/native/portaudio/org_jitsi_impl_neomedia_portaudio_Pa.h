/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_jitsi_impl_neomedia_portaudio_Pa */

#ifndef _Included_org_jitsi_impl_neomedia_portaudio_Pa
#define _Included_org_jitsi_impl_neomedia_portaudio_Pa
#ifdef __cplusplus
extern "C" {
#endif
#undef org_jitsi_impl_neomedia_portaudio_Pa_LATENCY_HIGH
#define org_jitsi_impl_neomedia_portaudio_Pa_LATENCY_HIGH -1.0
#undef org_jitsi_impl_neomedia_portaudio_Pa_LATENCY_LOW
#define org_jitsi_impl_neomedia_portaudio_Pa_LATENCY_LOW -2.0
#undef org_jitsi_impl_neomedia_portaudio_Pa_LATENCY_UNSPECIFIED
#define org_jitsi_impl_neomedia_portaudio_Pa_LATENCY_UNSPECIFIED 0.0
/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    AbortStream
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_AbortStream
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    CloseStream
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_CloseStream
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    DeviceInfo_getDefaultHighInputLatency
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_DeviceInfo_1getDefaultHighInputLatency
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    DeviceInfo_getDefaultHighOutputLatency
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_DeviceInfo_1getDefaultHighOutputLatency
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    DeviceInfo_getDefaultLowInputLatency
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_DeviceInfo_1getDefaultLowInputLatency
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    DeviceInfo_getDefaultLowOutputLatency
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_DeviceInfo_1getDefaultLowOutputLatency
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    DeviceInfo_getDefaultSampleRate
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_DeviceInfo_1getDefaultSampleRate
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    DeviceInfo_getDeviceUIDBytes
 * Signature: (J)[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_DeviceInfo_1getDeviceUIDBytes
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    DeviceInfo_getHostApi
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_DeviceInfo_1getHostApi
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    DeviceInfo_getMaxInputChannels
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_DeviceInfo_1getMaxInputChannels
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    DeviceInfo_getMaxOutputChannels
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_DeviceInfo_1getMaxOutputChannels
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    DeviceInfo_getNameBytes
 * Signature: (J)[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_DeviceInfo_1getNameBytes
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    DeviceInfo_getTransportTypeBytes
 * Signature: (J)[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_DeviceInfo_1getTransportTypeBytes
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    free
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_free
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    GetDefaultInputDevice
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_GetDefaultInputDevice
  (JNIEnv *, jclass);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    GetDefaultOutputDevice
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_GetDefaultOutputDevice
  (JNIEnv *, jclass);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    GetDeviceCount
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_GetDeviceCount
  (JNIEnv *, jclass);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    GetDeviceInfo
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_GetDeviceInfo
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    GetHostApiInfo
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_GetHostApiInfo
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    GetSampleSize
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_GetSampleSize
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    GetStreamReadAvailable
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_GetStreamReadAvailable
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    GetStreamWriteAvailable
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_GetStreamWriteAvailable
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    HostApiInfo_getDefaultInputDevice
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_HostApiInfo_1getDefaultInputDevice
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    HostApiInfo_getDefaultOutputDevice
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_HostApiInfo_1getDefaultOutputDevice
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    HostApiInfo_getDeviceCount
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_HostApiInfo_1getDeviceCount
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    HostApiInfo_getType
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_HostApiInfo_1getType
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    Initialize
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_Initialize
  (JNIEnv *, jclass);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    IsFormatSupported
 * Signature: (JJD)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_IsFormatSupported
  (JNIEnv *, jclass, jlong, jlong, jdouble);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    OpenStream
 * Signature: (JJDJJLorg/jitsi/impl/neomedia/portaudio/PortAudioStreamCallback;)J
 */
JNIEXPORT jlong JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_OpenStream
  (JNIEnv *, jclass, jlong, jlong, jdouble, jlong, jlong, jobject);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    ReadStream
 * Signature: (J[BJ)V
 */
JNIEXPORT void JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_ReadStream
  (JNIEnv *, jclass, jlong, jbyteArray, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    setDenoise
 * Signature: (JZ)V
 */
JNIEXPORT void JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_setDenoise
  (JNIEnv *, jclass, jlong, jboolean);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    setEchoFilterLengthInMillis
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_setEchoFilterLengthInMillis
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    StartStream
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_StartStream
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    StopStream
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_StopStream
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    StreamParameters_new
 * Signature: (IIJD)J
 */
JNIEXPORT jlong JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_StreamParameters_1new
  (JNIEnv *, jclass, jint, jint, jlong, jdouble);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    UpdateAvailableDeviceList
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_UpdateAvailableDeviceList
  (JNIEnv *, jclass);

/*
 * Class:     org_jitsi_impl_neomedia_portaudio_Pa
 * Method:    WriteStream
 * Signature: (J[BIJI)V
 */
JNIEXPORT void JNICALL Java_org_jitsi_impl_neomedia_portaudio_Pa_WriteStream
  (JNIEnv *, jclass, jlong, jbyteArray, jint, jlong, jint);

#ifdef __cplusplus
}
#endif
#endif
