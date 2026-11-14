# IxiGo UI React

## Conventions

### Folder structure

- Create multiple folders / subfolders to better structe the project:

  - assets: this folder / subfolders contains the images used in the UI
  - common: this folder / subfolders contains common components that can be re-used in the UI project
  - contents: this folder / subfolders contains the components that display the "contents" of the tabs on the root page
  - lib: this folder / subfolder contains the base "code library" that can be used to perform API calls, manage HTTP queries results / statuses, constants etc.
  - services: this folder / subfolders contains logic which act as "glue" between the component/s and the library code. Here we can define / find for examples the hooks which will use the library code to send / retrieve the data from / to the backend

- Every folder should contain if possible at least these three files:
  - index.ts: This file will export the rest of the files contained in the current folder, making them easier to import in other parts of the code
  - interfaces.ts: This will contain the definition of the "interfaces" used in the logic of "the current folder"
  - [Hook].ts or [Component.tsx]: Here is where we write our business logic or UI logic

### Naming conventions

- Fodlers word should be separated witha "-", example: dem-manager
- Interfaces should start with a capital "I", example: ISteamUser
- Component files should follow the "PamelCase" syntax. [reference](https://khalilstemmler.com/blogs/camel-case-snake-case-pascal-case/)
- Hook files shuold follow the "camelCase" syntax. [reference](https://khalilstemmler.com/blogs/camel-case-snake-case-pascal-case/)

### Custom components

- This project is using the [Material UI library](https://mui.com/material-ui/getting-started/) which provides multiple components out from the box, don't "reinvent the wheel"
- If you find yourself writing multiple lines of inline HTML to create a "custom component", perhaps you should create a proper component
