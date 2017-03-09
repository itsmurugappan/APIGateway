
@RestController
@Log
class Application {


    int counter = 0

    @RequestMapping(value = "/counter", produces = "application/json")
    String produce() {
        counter++
        log.info("Produced a value: ${counter}")

        "{\"value\": ${counter}}"
    }
}
