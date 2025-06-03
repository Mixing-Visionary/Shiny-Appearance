package ru.visionary.mixing.shiny_appearance.util

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.S3ClientOptions
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import java.util.Date
import ru.visionary.mixing.shiny_appearance.BuildConfig

fun getImageUrl(uuid: String): String {
    val ACCESS_KEY = "UE5qIJj1lMzX9XEdrAI2"
    val SECRET_KEY = "4l540nk5IJsuvrUTasPqHlQtKuu92bOfc3gy2sNm"
    val MINIO_ENDPOINT = "http://185.188.182.20:9000"
    val BUCKET_NAME = "images"
    val credentials = BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)
    val s3Client = AmazonS3Client(credentials).apply {
        endpoint = MINIO_ENDPOINT
        setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).build())
    }
    val request = GeneratePresignedUrlRequest(BUCKET_NAME, uuid).apply {
        expiration = Date(System.currentTimeMillis() + 1000 * 60 * 60)
    }
    return s3Client.generatePresignedUrl(request).toString()
}