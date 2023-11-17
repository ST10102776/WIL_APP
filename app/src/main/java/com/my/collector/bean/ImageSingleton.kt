package com.my.collector.bean

class ImageSingleton private constructor() {
    lateinit var bytes: ByteArray

    companion object {
        var instance: ImageSingleton? = null
            get() {
                if (field == null) field = ImageSingleton()
                return field
            }
            private set
    }
}