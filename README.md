# Kotlin Cloud Store Notifier

This is a simple proof-of-concept demonstrating the basics on how to create a google cloud function that is invoked 
whenever a new file is dropped into a google cloud store bucket. 

The code itself is relatively simple. A class implementing CloudEventsFunction is created with an accept function. 
The accept function is provided with a CloudEvent instance that contains the information relating to the event. 

The data field of that CloudEvent instance is a UTF-8 JSON string encoded as a byte array which must be decoded. 
The JSON includes a bunch of information about the event including a "bucket" field with the name of the bucket into
which the file was created and a name field containing the name of the file that was created within the bucket. 

In this example Jackson is used to unmarshal this JSON into an EventData data class containing just the bucket and name. 
This is then printed to the console. 

## Fat Jar

The gradle build provides the "shadow" plugin that can be used to produce a fat jar containing not just the compiled
classes of the project, but also all class files from all dependencies of the project, including the kotlin std lib. 
This is required for GCP's cloud functions Java support. 

In order to build the deployment jar simply run: 

`./gradlew clean shadowJar`

This will create the fat jar and place it in build/libs. 

## Deployment

Once the fat jar is built, the function needs to be deployed to GCP. The command for this is: 

`gcloud functions deploy <FUNCTION_NAME> --gen2 --runtime=java17 --region=<REGION_NAME> --source=build/libs --entry-point=gcp.store.function.NotifyOnFileFunction --memory=512MB --trigger-event-filters="type=google.cloud.storage.object.v1.finalized" --trigger-event-filters="bucket=<NAME_OF_TRIGGER_BUCKET>"`

In my tests, the first deploy using this command asked to confirm the enabling of various GCP APIs required to support
the function. After this, repeating the command (after rebuilding the jar) simply updated the function. 

## Testing

Once deployed, upload a new file to the configured bucket and observe that a message is printed into the cloud function
logs describing the bucket and the file that was uploaded. 