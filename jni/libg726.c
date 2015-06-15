#include <jni.h>
#include "g726_lib.h"

#include<android/log.h>

#define LOG_TAG "debug"
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, fmt, ##args)

/* Header for class com_shilangtech_diankan_util_G726DecodeJni */

#ifndef _Included_com_shilangtech_diankan_util_G726DecodeJni
#define _Included_com_shilangtech_diankan_util_G726DecodeJni
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_shilangtech_diankan_util_G726DecodeJni
 * Method:    g726_Encode
 * Signature: ([B[B)V
 */
JNIEXPORT void JNICALL Java_com_shilangtech_diankan_util_G726DecodeJni_g726_1Encode
  (JNIEnv * env, jobject this, jbyteArray buff1, jbyteArray buff2) {
	int size;
	 jbyte * speech = (jbyte*)(*env)->GetByteArrayElements(env,buff1,0);
	 jbyte * stream = (jbyte*)(*env)->GetByteArrayElements(env, buff2, 0);
	 //jbyte * arrayBody1 = (*env)->GetByteArrayElements(env,buff2,0);
	 g726_Encode(speech, stream, &size, 16);

	(*env)->ReleaseByteArrayElements(env, buff1, speech, 0);
	(*env)->ReleaseByteArrayElements(env, buff2, stream, 0);

}

/*
 * Class:     com_shilangtech_diankan_util_G726DecodeJni
 * Method:    g726_Decode
 * Signature: ([B[B)V
 */
JNIEXPORT void JNICALL Java_com_shilangtech_diankan_util_G726DecodeJni_g726_1Decode
  (JNIEnv *env, jobject this, jbyteArray buff1, jbyteArray buff2) {

	 jbyte * stream = (jbyte*)(*env)->GetByteArrayElements(env,buff1,0);
	 jbyte * speech = (jbyte*)(*env)->GetByteArrayElements(env, buff2, 0);
	 //jbyte * arrayBody1 = (*env)->GetByteArrayElements(env,buff2,0);
	 g726_Decode(stream, speech, 16);

	(*env)->ReleaseByteArrayElements(env, buff1, stream, 0);
	(*env)->ReleaseByteArrayElements(env, buff2, speech, 0);
}

#ifdef __cplusplus
}
#endif
#endif
