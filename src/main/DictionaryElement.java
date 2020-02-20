package main;

class DictionaryElement {
    String englishWord;
    String meanings;

    DictionaryElement(String englishWord, String meanings) {
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
        else {
            return new DictionaryElement(arg[0], "");
        }
    }
}
