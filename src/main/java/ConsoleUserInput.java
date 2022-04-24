import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleUserInput {
    private BufferedReader bufferedReader;
    private String url;
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
        System.out.println("Enter url:");
        try{
            this.url = bufferedReader.readLine();
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
        System.out.println("Do you want to determine in what File you want to save the crawled data? (y/n)");
        try {
            saveFilePath = bufferedReader.readLine();
            if (saveFilePath.toUpperCase().equals("Y")) {
                System.out.println("Enter the file path: (Path or Name)");
                saveFilePath = bufferedReader.readLine();
            }
            else if (saveFilePath.toUpperCase().equals("N")){
                saveFilePath = "";
            }
            else {
                readSaveFilePathFromConsole();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private void readTargetLanguageFromConsole(){
        System.out.println("Do you want to have the Headers translated? (y/n)");
        try {
            String translateLanguage = bufferedReader.readLine();
            if (translateLanguage.toUpperCase().equals("Y")){
                System.out.println("Enter a Language in ISO639_1 Standard: (Bsp.: DE, EN, IT,...)");
                translateLanguage = bufferedReader.readLine();
                if (containsLanguage(translateLanguage)){
                    targetLanguage = Language.valueOf(translateLanguage);
                    System.out.println("ok language");
                }
                else {
                    readTargetLanguageFromConsole();
                }
            }else if (translateLanguage.toUpperCase().equals("N")){
                targetLanguage = null;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSearchDepth() {
        return searchDepth;
    }

    public void setSearchDepth(int searchDepth) {
        this.searchDepth = searchDepth;
    }

    public String getSaveFilePath() {
        return saveFilePath;
    }

    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    public Language getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(Language targetLanguage) {
        this.targetLanguage = targetLanguage;
    }
}

