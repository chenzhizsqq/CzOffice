package com.xieyi.etoffice.common

typealias onSuccess<Any> = (Any) -> Unit
typealias onFailure<ResultType, Any> = (ResultType, Any) -> Unit
