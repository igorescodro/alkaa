package com.escodro.alkaa.common.extension

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable

/**
 * Converts the given [Parcelable] to [ByteArray].
 *
 * @param parcelable parcelable to be converted
 */
inline fun <reified R : Parcelable> marshallParcelable(parcelable: R): ByteArray {
    val bundle = Bundle()
    bundle.putParcelable(R::class.java.name, parcelable)
    return marshall(bundle)
}

/**
 * Converts the given [ByteArray] to [Parcelable].
 *
 * @param bytes byte array to be converted
 */
inline fun <reified R : Parcelable> unmarshallParcelable(bytes: ByteArray): R? =
    unmarshall(bytes)
        .readBundle(ClassLoader.getSystemClassLoader())
        ?.run {
            classLoader = R::class.java.classLoader
            getParcelable(R::class.java.name)
        }

/**
 * Gets the [Bundle] with the [Parcelable] and marshalls to [ByteArray].
 *
 * @param bundle bundle containing the Parcelable to be converted
 */
@SuppressLint("Recycle")
fun marshall(bundle: Bundle): ByteArray =
    Parcel.obtain().use {
        it.writeBundle(bundle)
        it.marshall()
    }

/**
 * Gets the [ByteArray] and recreates the [Parcelable].
 *
 * @param bytes byte array to be converted
 */
@SuppressLint("Recycle")
fun unmarshall(bytes: ByteArray): Parcel =
    Parcel.obtain().apply {
        unmarshall(bytes, 0, bytes.size)
        setDataPosition(0)
    }

private fun <T> Parcel.use(block: (Parcel) -> T): T =
    try {
        block(this)
    } finally {
        this.recycle()
    }
