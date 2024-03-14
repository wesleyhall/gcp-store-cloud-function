package gcp.store.function

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.cloud.functions.CloudEventsFunction
import io.cloudevents.CloudEvent

@Suppress("unused")
class NotifyOnFileFunction : CloudEventsFunction {

    private val mapper = jacksonObjectMapper()

    override fun accept(cloudEvent: CloudEvent?) {
        cloudEvent?.data?.toBytes()?.decodeToString()?.let { data ->
            val eventData: EventData = mapper.readValue(data)
            println("File named ${eventData.name} created in bucket ${eventData.bucket}")
        }
    }
}