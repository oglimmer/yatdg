module.exports = {

  config: {
    SchemaVersion: "1.0.0",
    Name: "yatdg",
    Vagrant: {
      Box: "ubuntu/xenial64",
      Install: "maven openjdk-8-jdk-headless docker.io"
    },
  },

  software: {
    yatdg: {
      Source: "mvn",
      Artifact: "target/yatdg.war"
    },

    tomcat: {
      Source: "tomcat",
      Deploy: "yatdg"
    }
  }
}
