package board.backend;

import org.springframework.boot.SpringApplication;

public class TestBoardApplication {

    public static void main(String[] args) {
        SpringApplication.from(BoardApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
