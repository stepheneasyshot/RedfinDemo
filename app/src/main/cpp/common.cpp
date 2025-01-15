#include "include/common.h"
#include "include/termExec.h"

#define LOG_TAG "AndroidTerminalDemo"

int registerNativeMethods(JNIEnv *env, const char *className,
                          JNINativeMethod *gMethods, int numMethods) {
    jclass clazz;

    clazz = env->FindClass(className);
    if (clazz == nullptr) {
        LOGE("Native registration unable to find class '%s'", className);
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        LOGE("RegisterNatives failed for '%s'", className);
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;

    LOGI("JNI_OnLoad");
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    if (init_Exec(env) != JNI_TRUE) {
        LOGE("ERROR: init of Exec failed");
        return JNI_ERR;
    }

    LOGI("JNI_OnLoad finished");

    return JNI_VERSION_1_6;
}
