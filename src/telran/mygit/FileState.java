package telran.mygit;

public record FileState (String fileName, long size, String dateCreation,  String dateModification, StatusGit status) {
}
