module.exports = {

  config: {
    SchemaVersion: "1.0.0",
    Name: "yatdg",
    Vagrant: {
      Box: "ubuntu/xenial64",
      Install: "maven openjdk-8-jdk-headless docker.io"
    },
  },

  versions: {
    yatdg: {
      TestedWith: "3-jre-11"
    },
    tomcat: {
      Docker: "tomcat9-openjdk11-openj9",
      TestedWith: "7 & 9"
    }
  },
  
  software: {
    yatdg: {
      Source: "mvn",
      Artifact: "target/yatdg.war"
    },

    tomcat: {
      Source: "tomcat",
      DockerImage: "oglimmer/adoptopenjdk-tomcat",
      DockerMemory: "70M",
      Deploy: "yatdg"
    }
  }
}
