package com.washathomes.AppUtils.Modules

data class Languages(val results: ArrayList<Language>)
data class Language(val id: String?, val name: String?, val language_code: String?, val icons: String?)

data class UpdateLanguage(val language_code: String)
