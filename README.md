# heystk
Community Driven SIEM

# Why another SIEM Solution?
Whether directly or indirectly, security impacts us all. By making this a community driven project, we can help eachother by sharing our knowledge of normalizing our logs as well as creating rules based on security issues.

# Components

* Agent
* Engine-API
* Engine

## Agent
This is the application that collects and normalizes data. You can create as many of them as you need and point them to the Engine-API. Each agent has a set of normalization chains which contains a tree of normalizers. The agent takes a single input and normalizes it pulling out data into individual fields. The agent will then pool a number of normalizations together and zip them up before posting them to the Engine-API.

An agent is started by specifying a unique identifier for the agent, it will then read the configuration file specified by the identifier.conf. Once the agent starts, depending on *how* you start the agent, it will load the configuration and then begin handling input based on which agent type you started the agent as.

When you first startup the agent, it is suggested to run the TestGeneratorAgent which will generate a number of logs to verify connectivity through the system.

## Engine-API
This application abstracts the underlying queueing mechanism, it receives the posts from the agents, unzips them verifies a Checksum and then posts the underlying Normalizations to backend queue. The point of the engine is to allow for a number of Queue systems to be used without changing the underlying Agent configurations. You can also use this functionality to load balance over multiple Engine-API's for large integrations.

There is no processing done with this layer, but is necessary to decouple the underlying Queue system from the Agent itself. Since at least one Agent will be installed on each system to be monitored (maybe more depending on how many components are being monitored) it's simpler to change an intermediary layer than it would be to have to redeploy or re-configure each Agent in the field.

*TODO*: The Engine-API should also provide an ability to download the agents configuration thus making it a simpler configuration mechansim.

## Engine
This application reads from the Queue, pushes into a short term database and processes the records in it. The application should have a set of rules for handling correlation and plugins. Any number of engines should be runnable to support a wider scale if more normalizations are being processed. Each rule which fires will have a corresponding action, actions are actually plug-ins which can be developed and deployed for an unlimited amount of functionality.

The engine should have the ability to correlate normalizations together based on the short term database. When a rule fires (which is not a correlation rule) the specified actions will fire.

A rule can fire multiple actions however, a Normalization can only match one rule though.

## Action Plug-Ins
These plug-ins will be responsible for handling actions when a specific rule fires. Some concepts are as follows but can be developed by request.

* Store Long Term Storage
* Call into remote API
* Send E-Mail
* Create 'ticket'
