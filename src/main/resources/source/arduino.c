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
enum MethodEnum
    {
     Pin_getPin,
     Pin_getPinMode,
     Pin_setPinMode,
     Pin_getValue,
     Pin_setValue,
     size_M
    };
jmethodID methods[size_M];
bool gRunning = false;

JNIEXPORT void JNICALL Java_fakeArduino_JNIArduino_init
(JNIEnv *env, jobject arduino) {
    gArduino = (jobject)(*env)->NewGlobalRef(env, arduino);
    gEnv = env;
    {
        jclass c = (*gEnv)->GetObjectClass(gEnv, gArduino);
        assert(c != NULL);
        methods[Pin_getPin] = (*gEnv)->GetMethodID(gEnv, c, "getPin", "(I)LfakeArduino/Pin;");
        assert(methods[Pin_getPin] != NULL);
    }
    jclass pin_class = (*gEnv)->FindClass(gEnv, "LfakeArduino/Pin;");
    assert(pin_class != NULL);

    methods[Pin_getPinMode] = (*gEnv)->GetMethodID(gEnv, pin_class, "getPinMode", "()I");
    assert(methods[Pin_getPinMode] != NULL);

    methods[Pin_setPinMode] = (*gEnv)->GetMethodID(gEnv, pin_class, "setPinMode", "(I)V");
    assert(methods[Pin_setPinMode] != NULL);

    methods[Pin_getValue] = (*gEnv)->GetMethodID(gEnv, pin_class, "getValue", "(I)I");
    assert(methods[Pin_getValue] != NULL);

    methods[Pin_setValue] = (*gEnv)->GetMethodID(gEnv, pin_class, "setValue", "(II)V");
    assert(methods[Pin_setValue] != NULL);
}

JNIEXPORT void JNICALL Java_fakeArduino_JNIArduino_close
(JNIEnv *env, jobject arduino) {
    (*env)->DeleteGlobalRef(env, gArduino);
}

JNIEXPORT jboolean JNICALL Java_fakeArduino_JNIArduino_getRunning
(JNIEnv *env, jobject thiz) {
    return (jboolean)gRunning;
}

JNIEXPORT void JNICALL Java_fakeArduino_JNIArduino_setup
(JNIEnv *env, jobject thiz) {
    gRunning = true;
    setup();
}

JNIEXPORT void JNICALL Java_fakeArduino_JNIArduino_loop
(JNIEnv *env, jobject thiz) {
    loop();
}

void pinMode(int pin_idx, int mode) {
    jobject pin = (*gEnv)->CallObjectMethod(gEnv, gArduino, methods[Pin_getPin], (jint)pin_idx);
    assert(pin != NULL);
    (*gEnv)->CallVoidMethod(gEnv, pin, methods[Pin_setPinMode], (jint)mode);
}

void digitalWrite(int pin_idx, int value) {
    jobject pin = (*gEnv)->CallObjectMethod(gEnv, gArduino, methods[Pin_getPin], (jint)pin_idx);
    assert(pin != NULL);
    (*gEnv)->CallVoidMethod(gEnv, pin, methods[Pin_setValue], (jint)value, (jint)0);
}

int digitalRead(int pin_idx) {
    jobject pin = (*gEnv)->CallObjectMethod(gEnv, gArduino, methods[Pin_getPin], (jint)pin_idx);
    assert(pin != NULL);
    return (int)((*gEnv)->CallIntMethod(gEnv, pin, methods[Pin_getValue], (jint)0));
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
