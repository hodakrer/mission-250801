package org.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.io.*;
import java.util.regex.*;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {


    public static void main(String[] args) throws IOException {

        //json open
        File dir = new File("db/wiseSaying");
        if(!dir.exists()) dir.mkdirs();
        File file = new File("db/wiseSaying/lastId.txt");
        if(!file.exists()) file.createNewFile();

        //json 문자열 형식
        int lastId = 0;
        ArrayList<Idiom> listIdioms = new ArrayList<>();

        while(true){
            //명령 받기
            System.out.print("명령) ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String cmd = br.readLine().trim();
            if(cmd.equals("종료")){
                //1단계: 종료.
                br.close();
                break;
            }
            else if (cmd.equals("등록")) {
                // 디렉토리 준비
                Path dirPath = Paths.get("db/wiseSaying");
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath);
                }

                // lastId.txt에서 마지막 ID 읽기
                Path lastIdPath = dirPath.resolve("lastId.txt");
                if (Files.exists(lastIdPath)) {
                    String content = Files.readString(lastIdPath).trim();
                    if (!content.isEmpty()) {
                        lastId = Integer.parseInt(content);
                    }
                }

                // 다음 ID 계산
                int number = lastId + 1;

                // 사용자 입력 받기
                System.out.print("명언 : ");
                String idiom = br.readLine();
                System.out.print("작가 : ");
                String author = br.readLine();

                // 객체 생성 및 리스트 추가
                listIdioms.add(new Idiom(number, idiom, author));

                // lastId.txt 갱신
                Files.writeString(lastIdPath,
                        String.valueOf(number),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);

                // 명언 JSON 파일 저장 (수동 직렬화)
                // 미션 9단계
                String json = "{\n" +
                        "  \"id\": " + number + ",\n" +
                        "  \"content\": \"" + idiom.replace("\"", "\\\"") + "\",\n" +
                        "  \"author\": \"" + author.replace("\"", "\\\"") + "\"\n" +
                        "}";
                Path jsonPath = dirPath.resolve(number + ".json");
                Files.writeString(jsonPath,
                        json,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);

                // 출력
                System.out.printf("%d번 명언이 등록되었습니다.\n", number);
            }
            else if(cmd.equals("목록")){
                //5단계: 목록.
                System.out.println("---------------------");
                for(Idiom item : listIdioms) {
                    System.out.printf("%d / %s / %s", item.getNumber(), item.getAuthor(), item.getIdiom()).println();
                }
            }
            else if(cmd.contains("삭제")){
                //6단계: 명언삭제.
                boolean erased = false;
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(cmd);
                int numTar = 0;
                if(matcher.find()){
                    numTar = Integer.parseInt(matcher.group());
                }

                for(int i = 0; i < listIdioms.size(); i ++){
                    if(listIdioms.get(i).getNumber() == numTar){
                        listIdioms.remove(i);
                        erased = true;
                        break;
                    }
                }
                //7단계: 존재하지 않는 명언삭제에 대한 예외처리.
                if(erased == false) System.out.printf("%d번 명언은 존재하지 않습니다.\n", numTar);
                else System.out.printf("%d번 명언이 삭제되었습니다.\n", numTar);
            }
            else if(cmd.contains("수정")){
                //8단계: 명언수정.
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(cmd);
                int numTar = 0;
                if(matcher.find()){
                    numTar = Integer.parseInt(matcher.group());
                }

                for(int i = 0; i < listIdioms.size(); i ++){
                    if(listIdioms.get(i).getNumber() == numTar){
                        System.out.printf("명언(기존) : %s\n", listIdioms.get(i).getIdiom());
                        System.out.print("명언 : ");
                        String idiom = br.readLine();
                        listIdioms.get(i).setIdiom(idiom);
                        System.out.println();
                        System.out.printf("작가(기존) : %s\n", listIdioms.get(i).getAuthor());
                        System.out.print("작가 : ");
                        String author = br.readLine();
                        listIdioms.get(i).setAuthor(author);
                        System.out.println();
                        break;
                    }
                }
            }
            //미션 10단계
            else if (cmd.equals("빌드")) {
                // JSON 배열 문자열 구성
                StringBuilder sb = new StringBuilder();
                sb.append("[\n");
                for (int i = 0; i < listIdioms.size(); i++) {
                    Idiom item = listIdioms.get(i);
                    sb.append("  {\n");
                    sb.append("    \"id\": ").append(item.getNumber()).append(",\n");
                    sb.append("    \"content\": \"").append(item.getIdiom().replace("\"", "\\\"")).append("\",\n");
                    sb.append("    \"author\": \"").append(item.getAuthor().replace("\"", "\\\"")).append("\"\n");
                    sb.append("  }");

                    // 마지막 항목이 아니면 쉼표 추가
                    if (i < listIdioms.size() - 1) {
                        sb.append(",");
                    }
                    sb.append("\n");
                }
                sb.append("]");

                // 파일로 저장
                Path outputPath = Paths.get("db/wiseSaying/data.json");
                Files.writeString(outputPath, sb.toString(),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);

                System.out.println("data.json 파일의 내용이 갱신되었습니다.");
            }
            else{
                System.out.println("알 수 없는 명령입니다.");
            }
        }

    }

}