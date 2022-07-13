# Overview

This is an example of credentials plugin for KdbInsideBrains intelliJ Idea Plugin.

The plugin is useless as default implementation is able to get process system properties but it shows an idea how
any other credentials plugin can be implemented.

The plugin defines a panel with 2 fields: username and ComboBox with list of all system properties for password
selection.

# How To

- Download and compile source code of the plugin to get binaries
- Update plugin.version in pom.xml as well as if it differs from hardcoded
- If you build own plugin, don't forget update CredentialsProvider value in pom.xml for maven-jar-plugin.