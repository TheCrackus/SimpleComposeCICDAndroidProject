This an Android Compose Project that uses CI/CD processes as:
- Multi Git branching (master and dev).
- Automated build processes when a new PR is created in both branches for CI.
- Codecov usage to apply code coverage rule.
- Github actions using YML files to check builds, lint and Jacoco reports.
- Github rule, if build fails merge is blocked.
- Github rule, if code covergare ratio (Unit testing) does not reach 80%, merge is blocked.
- Firebase products usage to report analytics logs and crashes.
- Firabase version publishing (CD) (dev branch).
- Play store publishing (CD) (master branch).
