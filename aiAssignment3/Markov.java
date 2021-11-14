package aiAssignment3;

import java.util.*;

public class Markov {
    private Map<String, HashMap<String, Integer>> biGramModel;
    private Map< HashMap<String, String> , HashMap<String, Integer> > triGramModel;
    private Map< HashMap<String, String> , HashMap<String, Integer> > usedSentence;
    String story;
    String story2;
    String[] storywords;
    String[] storywords2;
    StringBuilder myStory = new StringBuilder();

    public Markov(String story, String story2) {
        this.story = story;
        this.story2 = story2;
        storywords = story.split("\\s+");
        storywords2 = story2.split("\\s+");
        usedSentence = new HashMap<>();
        createMarkov(story);
        createMarkov(story2);
    }

    public String randomWord() {
        Random random = new Random();
        int whichStory = random.nextInt(2);
        if (whichStory == 0) {
            return storywords[random.nextInt(storywords.length)];
        } else
            return storywords2[random.nextInt(storywords2.length)];
    }

    public void WriteAStory() {

        String pastpast = randomWord();
        String past = "";
        String current = "";

        HashMap<String, Integer> potentialWord = biGramModel.get(pastpast);
        int highestCount = 0;

        for (Map.Entry<String, Integer> entry : potentialWord.entrySet()) {
            if (entry.getValue() > highestCount) {
                highestCount = entry.getValue();
                past = entry.getKey();
            }
        } //at this point you have found pastpast & past. now to find currents until limit.

        current = findNextWord(pastpast, past);

        System.out.print(pastpast + " " + past + " " + current );
        myStory.append(pastpast + " " + past + " " + current);
        for (int i = 3; i <=2000; i++) { //2000 word limit
            pastpast = past;
            past = current;
            current = findNextWord(pastpast, past);
            if (i % 10 == 0) {
                System.out.println(" " + current);
                myStory.append(" " + current + "\n");
            } else {
                System.out.print(" " + current);
                myStory.append(" " + current);
            }
        }
    }

    public String findNextWord(String pastpast, String past) {
        int highestCount = 0;
        String current = "";
        HashMap<String,String> triGramKey = new HashMap<>();
        triGramKey.put(pastpast, past);
        HashMap<String, Integer> triGramValue = triGramModel.get(triGramKey);

        if (triGramValue == null) {
            return  randomWord();
        }

        for (Map.Entry<String, Integer> entry : triGramValue.entrySet()) {
            if (entry.getValue() > highestCount) {
                highestCount = entry.getValue();
                current = entry.getKey();
            }
        }

        if (usedSentence.containsKey(triGramKey)) {
            HashMap<String, Integer> temp = usedSentence.get(triGramKey);
            if (temp == null) {
                temp = new HashMap<>();
                temp.put(current, 1);
            } else if (temp.containsKey(current)) {
                temp.replace(current, temp.get(current) + 1);
            } else {
                temp.put(current, 1);
            }
            usedSentence.replace(triGramKey, temp);
            if (temp.get(current) >= 1) {
                return randomWord();
            }
        } else { //if trigram model does NOT have tempkey, add to it with tempkey.
            HashMap<String, Integer> newTriGramValue = new HashMap<>();
            newTriGramValue.put(current, 1);
            usedSentence.put(triGramKey, newTriGramValue);
        }

        return current;
    }

    private void createMarkov(String currentStory) {
        biGramModel = new HashMap<>();
        triGramModel = new HashMap<>();
        Scanner scanner = new Scanner(currentStory);

        String pastpast = scanner.next();
        String past = scanner.next();
        String current = scanner.next();
        updateModels(pastpast, past, current);

        while (scanner.hasNext()) {
            pastpast = past;
            past = current;
            current = scanner.next();

            updateModels(pastpast, past, current);
        }

    }

    private void updateModels(String pastpast, String past, String current) {
        if (biGramModel.containsKey(pastpast)) { //update the bigram model
            HashMap<String, Integer> temp = biGramModel.get(pastpast);
            if (temp == null) {
                temp = new HashMap<>();
                temp.put(past, 1);
            } else {
                if (temp.containsKey(past)) {
                    int count = temp.get(past) + 1;
                    temp.replace(past, count);
                } else {
                    temp.put(past, 1);
                }
            }
            biGramModel.replace(pastpast, temp);
        } else {
            HashMap<String, Integer> temp = new HashMap<>();
            temp.put(past, 1);
            biGramModel.put(pastpast, temp);
        } //once updated, update the trigram model with the pastpast, past, and current string.
        //create the trigram key of two values:
        HashMap<String, String> tempKey = new HashMap<>();
        tempKey.put(pastpast, past);
        //now check if the trigram model has this tempKey.
        if (triGramModel.containsKey(tempKey)) {
            HashMap<String, Integer> tempValues = triGramModel.get(tempKey);
            if (tempValues == null) { //if values are null/empty, then create new hashmap
                tempValues = new HashMap<>();
                tempValues.put(current, 1);
            } else if (tempValues.containsKey(current)) { //if not null and contains current,
                int count = tempValues.get(current) + 1; //increment
                tempValues.replace(current, count); //replace with new count.
            } else { //if not null but does not contain current, add it.
                tempValues.put(current, 1);
            }
            triGramModel.replace(tempKey, tempValues);
        } else { //if trigram model does NOT have tempkey, add to it with tempkey.
            HashMap<String, Integer> tempValues = new HashMap<>();
            tempValues.put(current, 1);
            triGramModel.put(tempKey, tempValues);
        }
    }

    public String getMyStory() {
        return myStory.toString();
    }

}
