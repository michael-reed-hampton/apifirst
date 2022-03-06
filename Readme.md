1. Use https://start.spring.io/ to get started.
2. [Generate] creates a zip, named apifirst.zip in this case.
3. Unzip into the root of the repo.../Users/mike/Github/apifirst
4. Open in intellij
5. Installed Java 17 (because directions said it would be used)

`sdk install java`

6. Try to build it immediately...

`./gradlew build`

it worked

7. Try to run it immediately...

`./gradlew bootRun`

it worked - http://localhost:8080/

8.  Set up git.
    `git init`
9.  Add .gitignore to root of project.
10. Add files to git.
11. Commit all added files/dirs to git.
12. Push the initial branch up to git.  Created it on github, then...
    `git remote add origin https://github.com/michael-reed-hampton/apifirst.git`
    and
    `git push -u origin master`
13. Create branch to get going with 'real' code now. `git checkout -b add_openapi`
14. Try to build it `./gradlew build`
15. Try to run it `./gradlew bootRun`
16.  Look at the docs page - http://localhost:8080/swagger-ui.html
17.  Commit the changes to the branch.
18.  Push the branch.  Create a Pull Request, check it (here we would have someone review it).  Merge it.
19.  Checkout the master branch and pull down the changes.
     `git checkout master`
     `git pull origin master`
     


