# Settings

A library for loading settings from environment variables.


## Using with maven

Add the dependency:
```xml
<dependency>
    <groupId>de.fraunhofer.iosb.ilt</groupId>
    <artifactId>Settings</artifactId>
    <version>1.0</version>
</dependency>

```


## Using with gradle

Add the dependency:
```gradle
compile 'de.fraunhofer.iosb.ilt:Settings:1.0'
```


## Core Usage

Classes that hold names of environment variables implement the interface `ConfigDefaults`.

Constants that define names of environment variables are annotated with the default value
of the variable, and optionally if the variable is a sensitive variable and should not have
its value logged.

```java
public class SettingsHolder implements ConfigDefaults {

    @DefaultValue("")
    public static final String NAME_VAR_USERNAME = "username";

    @DefaultValue("")
    @SensitiveValue
    public static final String NAME_VAR_PASSWORD = "password";

    @DefaultValueBoolean(false)
    public static final String NAME_LOG_SENSITIVE_DATA = "logSensitiveData";

    @DefaultValueInt(42)
    public static final String NAME_VAR_PORT = "port";
}
```

Next, the application can create a `Settings` or `CachedSettings` instance and use it to access environment variables:

```java
Settings settings = new CachedSettings();
String username = settings.get(NAME_VAR_USERNAME, SettingsHolder.class);
String password = settings.get(NAME_VAR_PASSWORD, SettingsHolder.class);
int port = settings.getInt(NAME_VAR_PORT, SettingsHolder.class);
```

The first time any environment variable is requested, its name and the value are logged.
When a setting is tagged as `@SensitiveValue` the value is not logged, unless first explicitly enabled.
This can be very useful for debugging and the setting itself can be loaded from an environment variable:

```java
Settings settings = new CachedSettings();
boolean logSensitiveData = settings.getBoolean(NAME_LOG_SENSITIVE_DATA, SettingsHolder.class);
settings.setLogSensitiveData(logSensitiveData);
```

TODO: Document the use of namespaces and `ConfigProvider`.
