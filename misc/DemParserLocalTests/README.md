# Parse the DEM files locally

## Requirements

- [Visual Studio CE 2019](https://visualstudio.microsoft.com/vs/community/)
- [Eclipse](https://www.eclipse.org/downloads/packages/release/2021-03/r/eclipse-ide-enterprise-java-and-web-developers)
- [Eclipse Spring Tools](https://marketplace.eclipse.org/content/spring-tools-4-aka-spring-tool-suite-4)
- [PostgreSQL](https://www.postgresql.org/) or [Docker](https://www.docker.com/)

## Running Locally

**Info:** On my machine I run the DB in a Docker cointainer

- Copy the repository Link
  ![Repo Link](./1.png)

- Use Eclipse to clone the repository
  ![Repo Link](./2.png)

- Import the projects
  ![Repo Link](./3.png)
  ![Repo Link](./4.png)

- Try to work on a different branch and then once ready create a Pull Request to push the changes into the main branch
  ![Repo Link](./6.png)

- Open the DemoParser.sln file with Visual Studio
 ![Repo Link](./7.png)

- Compile the project
 ![Repo Link](./8.png)

- Check for the new folders created by the compile process
 ![Repo Link](./9.png)

### Build the Java Projects

- Install the Eclipse STS tool
 ![Repo Link](./10.png)
 ![Repo Link](./11.png)

- Compile the Maven Project
  ![Repo Link](./12.png)
  ![Repo Link](./13.png)
  ![Repo Link](./14.png)

- Use the docker-compose file to start the Docker Container
  ![Repo Link](./15.png)
  ![Repo Link](./17.png)

- Use the Boot Dashboard to strat the "RoundParser" service
  ![Repo Link](./16.png)

### Info

The application.yml file contains some properties that you can change when running it locally.
![Repo Link](./18.png)
