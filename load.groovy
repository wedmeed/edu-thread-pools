
import groovy.json.JsonOutput

def rand = new Random();

def suitName = word(3+rand.nextInt(4)) + "_" + word(3+rand.nextInt(4));
vars.put("suitName",suitName);

def taskAmount = rand.nextInt(20)
def taskSet = new LinkedList()
for (int i=0; i<taskAmount; i++){
    def task = new HashMap()
    def computation = rand.nextInt(10)
    def deadline = rand.nextInt(300)+computation
    def period = rand.nextInt(1000)+deadline
    task.put("name", "task"+i)
    task.put("computation", computation)
    task.put("deadline", deadline)
    task.put("period", period)
    task.put("priority", rand.nextInt(5))
    taskSet.add(task)
}

def body = JsonOutput.prettyPrint(JsonOutput.toJson(taskSet))
vars.put("body",body);


static def word(length) {
    def rand = new Random();
    def consonants = "bcdfghjlmnpqrstv";
    def vowels = "aeiou";
    def builder = new StringBuilder();
    for (int i=0; i<length/2; i++) {
        def randConsonant = String.valueOf(consonants.charAt(rand.nextInt(consonants.length())));
        def randVowel = String.valueOf(vowels.charAt(rand.nextInt(vowels.length())));
        if (i == 0){
            randConsonant = randConsonant.toUpperCase();
        }
        builder.append(randConsonant);
        if (i*2 < length-1){
            builder.append(randVowel);
        }
    }
    return builder.toString();
}
