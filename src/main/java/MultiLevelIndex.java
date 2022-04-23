import java.util.Arrays;

public class MultiLevelIndex {
    private int[] indices;
    public MultiLevelIndex(){
        this.indices = new int[0];
    }

    private MultiLevelIndex(int[] array){
        this.indices = Arrays.copyOf(array, array.length);
    }

    @Override
    public String toString() {
        return String.join(".", toStringArray(indices));
    }

    private String[] toStringArray(int[] array){
        String[] result = new String[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.toString(array[i]);
        }
        return result;
    }

    public MultiLevelIndex nextIndex(int levelDepth){
        int[] array = Arrays.copyOf(indices, levelDepth);
        array[array.length - 1]++;

        for (int i = 0; i < array.length; i++) {
            if (array[i] == 0) {
                array[i] = 1;
            }
        }

        return new MultiLevelIndex(array);
    }
}
