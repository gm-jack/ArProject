cmake_minimum_required(VERSION 3.4.1)

include(ndk-stl-config.cmake)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/)
include_directories(${PROJECT_SOURCE_DIR}/jni)
set(src_list ${PROJECT_SOURCE_DIR}/jni/yuv-decoder.c)
add_library(gpuimage-library SHARED ${src_list})

find_library(log-lib log)
target_link_libraries(gpuimage-library ${log-lib})
