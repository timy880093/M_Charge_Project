# 使用 Tomcat 作為基礎映像
FROM tomcat:8.5.93-jre8-temurin-jammy

# 複製 ROOT.war 到 Tomcat 的 webapps 目錄
COPY target/ROOT.war /usr/local/tomcat/webapps/