package com.app.frimline.Common

interface DownloadListener {
    fun onSuccess(path: String)
    fun onFailure(error: String)
}