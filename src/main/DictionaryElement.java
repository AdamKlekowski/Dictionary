package main;

public class DictionaryElement {
    public String englishWord;
    public String meanings;

    public DictionaryElement(String englishWord, String meanings) {
        this.englishWord = englishWord;
        this.meanings = meanings;
    }

    public String toString() {
        return englishWord + "<->" + meanings;
    }

    static DictionaryElement fromString(String line) {
        String[] arg = line.split("<->");
        if(arg.length == 2) {
            return new DictionaryElement(arg[0], arg[1]);
        }
        else if (arg.length == 1) {
            return new DictionaryElement(arg[0], "");
        } else {
            throw new IllegalArgumentException(line);
        }
    }
}
