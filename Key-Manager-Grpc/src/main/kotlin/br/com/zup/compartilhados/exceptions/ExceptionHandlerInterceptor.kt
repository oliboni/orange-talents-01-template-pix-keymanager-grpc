package br.com.zup.compartilhados.exceptions

import io.grpc.BindableService
import io.grpc.stub.StreamObserver
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExceptionHandlerInterceptor(@Inject private val resolver: ExceptionHandlerResolver) : MethodInterceptor<BindableService,Any?>{
    private val log = LoggerFactory.getLogger(ExceptionHandlerInterceptor::class.java)

    override fun intercept(context: MethodInvocationContext<BindableService, Any?>): Any? {
        return try {
            context.proceed()
        } catch (e: Exception){
            log.error("Handling the exception '${e.javaClass.name}' while processing the call: ${context.targetMethod}",e)

                val handler = resolver.resolver(e)
                val status = handler.handle(e)

                GrpcEndpointArguments(context).response().onError(status.asRuntimeException())

            null
        }
    }
}

class GrpcEndpointArguments(private val context: MethodInvocationContext<BindableService, Any?>) {
    fun response(): StreamObserver<*>{
        return context.parameterValues[1] as StreamObserver<*>
    }
}
