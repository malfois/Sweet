/*
    Debigulator - A batch compression utility
    Copyright (C) 2003-2014 Hugues Johnson

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ncd.fileviewer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.swing.filechooser.FileSystemView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class FilePathTreeItem extends TreeItem<String> {

	private boolean		isLeaf;
	private boolean		isFirstTimeChildren	= true;
	private boolean		isFirstTimeLeaf		= true;
	private final Path	path;

	public Path getPath() {
		return (this.path);
	}

	public String getAbsolutePath() {
		return (this.path.toFile().getAbsolutePath());
	}

	public boolean isDirectory() {
		return (this.path.toFile().isDirectory());
	}

	public FilePathTreeItem(Path path) {
		super(path.toString());
		this.path = path;

		// set the value (which is what is displayed in the tree)
		String fullPath = this.getAbsolutePath();
		if (!fullPath.endsWith(File.separator)) {
			fullPath = path.getFileName().toString();
			int indexOf = fullPath.lastIndexOf(File.separator);
			if (indexOf > 0) {
				fullPath = fullPath.substring(indexOf + 1);
			}
		}

		File f = path.toFile();
		FileSystemView view = FileSystemView.getFileSystemView();
		String name = view.getSystemDisplayName(f);
		if (!name.equalsIgnoreCase(f.getName())) {
			fullPath = name + " " + f.getName();
		}

		this.setValue(fullPath);
	}

	@Override
	public ObservableList<TreeItem<String>> getChildren() {
		if (isFirstTimeChildren) {
			isFirstTimeChildren = false;
			super.getChildren().setAll(buildChildren(this));
		}
		return (super.getChildren());
	}

	@Override
	public boolean isLeaf() {
		if (isFirstTimeLeaf) {
			isFirstTimeLeaf = false;
			isLeaf = !this.hasDirectory(this.path.toFile());
		}
		return (isLeaf);
	}

	private ObservableList<FilePathTreeItem> buildChildren(FilePathTreeItem treeItem) {
		Path f = treeItem.getPath();
		if ((f != null) && (f.toFile().isDirectory())) {
			File[] files = f.toFile().listFiles();
			if (files != null) {
				ObservableList<FilePathTreeItem> children = FXCollections.observableArrayList();
				for (File childFile : files) {
					if (childFile.isDirectory()) {
						try {
							Path p = childFile.toPath().toRealPath();
							children.add(new FilePathTreeItem(p));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				return (children);
			}
		}
		return FXCollections.emptyObservableList();

	}

	private boolean hasDirectory(File file) {
		File[] files = file.listFiles();
		if (files == null) return false;
		Boolean directory = false;
		for (File f : files) {
			if (f.isDirectory()) {
				directory = true;
				break;
			}
		}
		return directory;
	}
}