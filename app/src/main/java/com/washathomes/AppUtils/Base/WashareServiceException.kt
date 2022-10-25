package com.washathomes.AppUtils.Base

/**
 * Created on 2019-12-16.
 */


class WashareServiceException : ServiceException {

    var error: Error? = null


    constructor(detailMessage: String) : super(detailMessage)

    constructor(error: Error) : super(error.toString()) {
        this.error = error
    }

}