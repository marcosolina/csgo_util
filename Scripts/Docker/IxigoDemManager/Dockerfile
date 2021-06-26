FROM maven:3.8.1-jdk-8 AS MAVEN_IMAGE
WORKDIR /tmp
RUN git clone -b refactoring https://github.com/marcosolina/csgo_util.git
RUN git clone https://github.com/marcosolina/javautils.git

RUN mvn clean install -f javautils/Utils
RUN mvn clean install -f ./csgo_util/IxigoDemManager/pom.xml


# Build the C# parser
FROM mcr.microsoft.com/dotnet/sdk:5.0 AS DEM_PARSER
WORKDIR /app

# Copy csproj and restore as distinct layers
COPY --from=MAVEN_IMAGE /tmp/csgo_util/DemParser/*.csproj ./
RUN dotnet restore

# Copy everything else and build
COPY --from=MAVEN_IMAGE /tmp/csgo_util/DemParser/* ./
RUN dotnet publish -c Release -o out -r linux-x64


FROM openjdk:11
EXPOSE 8081:8081
RUN mkdir /demFiles && mkdir /demParser
COPY --from=DEM_PARSER /app/out/* /demParser/
COPY --from=MAVEN_IMAGE /tmp/csgo_util/IxigoDemManager/target/IxigoDemManager*.jar IxigoDemManager.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh && chmod +x /demParser/DemParser


ENTRYPOINT ["/start.sh"]

