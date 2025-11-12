import controller.Controller;
import repo.ArrayRepo;
import repo.Repository;
import view.TextUI;

void main() {
    Repository repo = new ArrayRepo();
    Controller controller = new Controller(repo);
    TextUI textUI = new TextUI(controller);
    textUI.start();
}