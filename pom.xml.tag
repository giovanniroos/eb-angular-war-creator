<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>za.co.discovery.eb.web</groupId>
  <artifactId>eb-angular-war-creator</artifactId>
  <version>1.0</version>
  <packaging>war</packaging>

  <name>EB Angular War Creator</name>
  <description>EB Create war from angular app</description>

  <scm>
    <connection>scm:git:http://bitbucket.discsrv.co.za/scm/debs/eb-angular-war-creator.git</connection>
    <developerConnection>scm:git:http://bitbucket.discsrv.co.za/scm/debs/eb-angular-war-creator.git
    </developerConnection>
    <url>http://bitbucket.discsrv.co.za/projects/DEBS/repos/eb-angular-war-creator/</url>
    <tag>v1.0</tag>
  </scm>

  <properties>
    <frontend-maven-plugin.version>1.6</frontend-maven-plugin.version>
    <environment>1.6</environment>
    <angular_build_dir>${angular.buildDIR}</angular_build_dir>
    <jboss_deploy_dir>${jboss.deployDIR}</jboss_deploy_dir>
  </properties>

  <dependencies>
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>3.0.1</version>
      <scope>provided</scope>
    </dependency>
  </dependencies>

  <build>
    <finalName>eb-web-employer-zone</finalName>
    <plugins>
      <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.1</version>
        <configuration>
          <source>1.8</source>
          <target>1.8</target>
        </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-war-plugin</artifactId>
        <version>2.6</version>
        <configuration>
          <failOnMissingWebXml>false</failOnMissingWebXml>
          <webResources>
            <resource>
              <!--<directory>C:\Users\lindikha1\Liver\Code\EBS\eb-portal-employer-onboarding\dist</directory>-->
              <directory>${angular_build_dir}</directory>
              <includes>
                <include>assets/**/*.*</include>
                <include>assets/img/*.*</include>
                <include>assets/css/*.*</include>
                <include>assets/js/**/*.*</include>
                <include>*.*</include>
              </includes>
            </resource>
            <resource>
              <directory>src/main/webapp</directory>
            </resource>
          </webResources>
          <outputDirectory>${jboss_deploy_dir}</outputDirectory>
        </configuration>
      </plugin>

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-release-plugin</artifactId>
        <version>2.5.1</version>
        <configuration>
          <tagNameFormat>v@{project.version}</tagNameFormat>
          <autoVersionSubmodules>true</autoVersionSubmodules>
          <releaseProfiles>releases</releaseProfiles>
        </configuration>
      </plugin>
    </plugins>
  </build>

  <profiles>
    <profile>
      <id>default</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <build>
        <plugins>
          <plugin>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>2.18.1</version>
            <configuration>
              <skip>true</skip>
            </configuration>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>

  <distributionManagement>
    <repository>
      <id>Releases</id>
      <name>Internal Release Repository</name>
      <url>http://dlpbuild01:8081/nexus/content/repositories/releases/</url>
    </repository>
    <snapshotRepository>
      <id>Snapshots</id>
      <name>Internal Snapshot Repository</name>
      <url>http://dlpbuild01:8081/nexus/content/repositories/snapshots/</url>
    </snapshotRepository>
  </distributionManagement>
</project>
