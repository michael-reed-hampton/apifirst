# Start With spring Boot

Note that this leverages 
* https://www.baeldung.com/kotlin/swagger-spring-rest-api
* https://reflectoring.io/spring-boot-openapi/

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
# Start making API First
1. Create branch to get going with 'api first' code now. `git checkout -b add_apifirst`
2. Add to our project build.gradle.kts.  We add libraries and tasks to generate the server.
3. Get the initial OpenAPI doc written up.
    1. Use the template https://github.com/michael-reed-hampton/openapi-template as a starting point
    2. For now just add it to the project.  Eventually we will have it in a separate repo.
4. Add the controller and a very simple delegate implementation.

Adding some testing
https://medium.com/backyard-programmers/kotlin-spring-boot-unit-testing-integration-testing-with-junit5-and-mockk-a2977bbe5711

    

     


