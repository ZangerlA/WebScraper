import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleUserInput {
    private BufferedReader bufferedReader;
    private String[] urls;
    private int searchDepth;
    private String saveFilePath;
    private Language targetLanguage;

    public ConsoleUserInput(){
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        readUrlFromConsole();
        readSearchDepthFromConsole();
        readSaveFilePathFromConsole();
        readTargetLanguageFromConsole();
    }

    private void readUrlFromConsole(){
        System.out.println("Enter urls separated with space: ");
        try{
            String input = bufferedReader.readLine().trim();
            this.urls = input.split(" ");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    private void readSearchDepthFromConsole(){
        System.out.println("Enter searchDepth: (Bsp: 1, 2, 3)");
        try{
            String userinput = bufferedReader.readLine();

            if (userinput.matches("[0-9]")) {
                this.searchDepth = Integer.parseInt(userinput);
            }
            else {
                readSearchDepthFromConsole();
            }

        }catch (IOException e){
            System.out.println(e.getMessage());
        }catch (NumberFormatException e){
            System.out.println();
        }
    }

    private void readSaveFilePathFromConsole(){
        System.out.println("Do you want to save the crawled data in a specific file? (y/n)");
        try {
            saveFilePath = bufferedReader.readLine();
            if (saveFilePath.toUpperCase().equals("Y")) {
                System.out.println("Enter the file path or name: (path or name)");
                saveFilePath = bufferedReader.readLine();
            }
            else if (saveFilePath.toUpperCase().equals("N")){
                saveFilePath = "default.md";
            }
            else {
                readSaveFilePathFromConsole();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private void readTargetLanguageFromConsole(){
        System.out.println("Do you want to translate the headers? (y/n)");
        try {
            String translateLanguage = bufferedReader.readLine();
            if (translateLanguage.toUpperCase().equals("Y")){
                System.out.println("Enter the target language in ISO639_1 standard: (Bsp.: DE, EN, IT,...)");
                translateLanguage = bufferedReader.readLine();
                if (containsLanguage(translateLanguage)){
                    targetLanguage = Language.valueOf(translateLanguage);
                }
                else {
                    readTargetLanguageFromConsole();
                }
            }else if (translateLanguage.toUpperCase().equals("N")){
                targetLanguage = Language.NONE;
            }else {
                readTargetLanguageFromConsole();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static boolean containsLanguage(String string){
        for (Language language: Language.values()) {
            if (language.name().equals(string)){
                return true;
            }
        }
        return false;
    }

    private void setTargetLanguageFromString(String input){
        if (Language.valueOf(input) != null){
            targetLanguage = Language.valueOf(input);
        }
    }

    public String[] getUrls() {
        return urls;
    }

    public int getSearchDepth() {
        return searchDepth;
    }

    public String getSaveFilePath() {
        return saveFilePath;
    }

    public Language getTargetLanguage() {
        return targetLanguage;
    }
}

