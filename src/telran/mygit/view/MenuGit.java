package telran.mygit.view;

import telran.mygit.*;
import telran.view.*;
import static telran.view.Item.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;

public class MenuGit {
	private static IGitRepository rep;	
	public static Menu getMenuItemsGit(IGitRepository rep) {
		MenuGit.rep = rep;
		return getUserMenu();
	}
	

	private static Menu getUserMenu() {
		return new Menu("MyGIT menu", 
				of("Commit", MenuGit::commit), 
				of("Info about current state", MenuGit::info),
				of("Switch to (branch / commit)", MenuGit::switchTo), 
				of("Create new branch", MenuGit::createBranch),
				of("Rename branch", MenuGit::renameBranch), 
				of("Delete branch", MenuGit::deleteBranch),
				of("Add pattern (regex) for files to ignore", MenuGit::addIgnoredFileNameExp),
				of("Display HEAD commit name", MenuGit::getHead),
				of("Display all branches and commits", MenuGit::displayTree), 
				of("Display branches", MenuGit::branches),
				of("Content of commit", MenuGit::commitContent), 
				of("Log (branch message - branch name) of the current branch", MenuGit::log),
				of("Exit", MenuGit::save, true)
				);
	}

	private static void commit(InputOutput io) {
		String message = io.readString("Input commit's message");
		io.writeLine(rep.commit(message));
	}

	private static String maxNameLength = "20";
	private static String maxSizeLength = "20";

	private static String patternTable(String[] fields) {
		return String.format("%" + maxNameLength + "s  %-" + maxSizeLength + "s  %30s  %30s  %9s\r\n", fields[0],
				fields[1], fields[2], fields[3], fields[4]);
	}

	private static void info(InputOutput io) {
		try {
			List<FileState> info = rep.info();
			if (info == null) {
				io.writeLine("No files to view (no new|modified files or empty folder)");
				return;
			}
			// define fields width
			Long strLength = info.stream().map(fs -> fs.fileName().toString()).mapToLong(String::length).max()
					.getAsLong();
			maxNameLength = getFieldWidth(strLength, "File name");
			Long sizeLength = info.stream().map(fs -> Long.toString(fs.size())).mapToLong(String::length).max()
					.getAsLong();
			maxSizeLength = getFieldWidth(sizeLength, "Size, byte");

			String[] res = { patternTable(new String[] { "File name", "Size, byte", "Creation timestamp",
					"Last modification timestamp", "Status" }) };
			info.forEach(s -> res[0] += patternTable(new String[] { s.fileName().toString(), Long.toString(s.size()),
					s.dateCreation().toString(), s.dateModification().toString(), s.status().name().toString() }));
			io.writeLine(res[0]);
		} catch (Exception e) {
			io.writeLine(e.getMessage());
		}
	}

	private static String getFieldWidth(Long longLength, String str) {
		longLength = longLength < str.length() ? str.length() : longLength;
		return longLength.toString();
	}

	private static void createBranch(InputOutput io) {
		String name = io.readString("Input branch name");
		io.writeLine(rep.createBranch(name));
	}

	private static void renameBranch(InputOutput io) {
		String name = io.readString("Input branch name to be renamed");
		String newName = io.readString("Input new branch name");
		io.writeLine(rep.renameBranch(name, newName));
	}

	private static void deleteBranch(InputOutput io) {
		String name = io.readString("Input branch name to be deleted");
		String reply = rep.deleteBranch(name);
		io.writeLine(reply);
		if (reply.contains("There's a conjunction on the branch")) {
			io.writeLine("Do you want to delete current branch and all subbranches? (y/n)");
			while (true) {
				reply = io.readString("Type 'y' or 'n'").toLowerCase();
				if (reply == "y") {
					io.writeLine(rep.deleteBranchForced(name));
					break;
				} else if (reply == "n") {
					io.writeLine("Branch " + name + " is not deleted");
					break;
				}
			}
		}
	}

	private static void log(InputOutput io) {
		io.writeLine(rep.log());
	}

	private static void branches(InputOutput io) {
		io.writeLine(rep.branches());
	}

	private static void commitContent(InputOutput io) {
		String commitName = io.readString("Input commit name");
		List<String> res = rep.commitContent(commitName);
		if (res == null) {
			io.writeLine("No commits in the current branch");
		} else {
			io.writeLine(res);
		}
	}

	private static void getHead(InputOutput io) {
		io.writeLine(rep.getHead());
	}

	private static void save(InputOutput io) {
		io.writeLine(rep.save() + ". Thank you and goodbye!");
	}

	private static void addIgnoredFileNameExp(InputOutput io) {
		String regex = io.readString("Input pattern for files to be ignored");
		io.writeLine(rep.addIgnoredFileNameExp(regex));
	}

	private static void switchTo(InputOutput io) {
		String name = io.readString("Input branch or commit name to be switched to");
		io.writeLine(rep.switchTo(name));
	}

	private static void displayTree(InputOutput io) {
		io.writeLine(rep.viewAllCommits());
	}

}
