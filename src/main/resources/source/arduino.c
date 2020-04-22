#define FOOBAR_COMPILER

#include "fakeArduino_JNIArduino.h"
#include "Arduino.h"
#include <assert.h>
#include <stdbool.h>

#ifdef WIN32
#include <windows.h>
#elif _POSIX_C_SOURCE >= 199309L
#include <time.h>   // for nanosleep
#else
#include <unistd.h> // for usleep
#endif

void sleep_us(int microseconds) // cross-platform sleep function
{
#ifdef WIN32
    Sleep(microseconds / 1000);
#elif _POSIX_C_SOURCE >= 199309L
    struct timespec ts;
    ts.tv_sec = microseconds / 1000000;
    ts.tv_nsec = (microseconds % 1000000) * 1000000 * 1000;
    nanosleep(&ts, NULL);
#else
    usleep(microseconds);
#endif
}

jobject gArduino;
JNIEnv *gEnv;
bool gRunning = false;

JNIEXPORT void JNICALL Java_fakeArduino_JNIArduino_init
(JNIEnv *env, jobject arduino) {
    gArduino = (jobject)(*env)->NewGlobalRef(env, arduino);
    gEnv = env;
}

JNIEXPORT void JNICALL Java_fakeArduino_JNIArduino_close
(JNIEnv *env, jobject arduino) {
    (*env)->DeleteGlobalRef(env, gArduino);
}

JNIEXPORT jboolean JNICALL Java_fakeArduino_JNIArduino_getRunning
(JNIEnv *env, jobject thiz) {
    return (jboolean)gRunning;
}

#define OurCallObjectMethod(env, thiz, name, sig, ret) {                \
        jclass c = (*(env))->GetObjectClass((env), (thiz));             \
        jmethodID m = (*(env))->GetMethodID((env), (c), (name), (sig)); \
        ret = (*(env))->CallObjectMethod((env), (thiz), m);             \
    }                                                                   \

JNIEXPORT void JNICALL Java_fakeArduino_JNIArduino_setup
(JNIEnv *env, jobject thiz) {
    gRunning = true;
    setup();
}

JNIEXPORT void JNICALL Java_fakeArduino_JNIArduino_loop
(JNIEnv *env, jobject thiz) {
    loop();
}

void digitalWrite(int pin_idx, int value) {
    jclass c = (*gEnv)->GetObjectClass(gEnv, gArduino);
    assert(c != NULL);
    jmethodID m = (*gEnv)->GetMethodID(gEnv, c, "getPin", "(I)LfakeArduino/Pin;");
    assert(m != NULL);
    jobject pin = (*gEnv)->CallObjectMethod(gEnv, gArduino, m, (jint)pin_idx);
    assert(pin != NULL);
    jclass pc = (*gEnv)->GetObjectClass(gEnv, pin);
    assert(pc != NULL);
    jmethodID pm = (*gEnv)->GetMethodID(gEnv, pc, "setValue", "(I)V");
    assert(pm != NULL);
    (*gEnv)->CallVoidMethod(gEnv, pin, pm, (jint)value);
}

void exitArduino() {
    gRunning = false;
}

void delay(int ms) {
    sleep_us(ms * 1000);
}

void delayMicroseconds(int us) {
    sleep_us(us);
}
