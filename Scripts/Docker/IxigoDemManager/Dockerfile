# Build the C# parser
FROM mcr.microsoft.com/dotnet/sdk:5.0 AS DEM_PARSER
WORKDIR /app

# Copy csproj and restore as distinct layers
COPY --from=my-maven-img /ixigo/csgo_util/DemParser/*.csproj ./
RUN dotnet restore

# Copy everything else and build
COPY --from=my-maven-img /ixigo/csgo_util/DemParser/* ./
RUN dotnet publish -c Release -o out -r linux-x64

# Putting all together
FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /demFiles && mkdir /demParser
COPY --from=DEM_PARSER /app/out/* /demParser/
COPY --from=my-maven-img /ixigo/csgo_util/IxigoDemManager/target/IxigoDemManager*.jar IxigoDemManager.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh && chmod +x /demParser/DemParser


ENTRYPOINT ["/start.sh"]

