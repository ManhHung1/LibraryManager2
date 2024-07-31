package org.example.interfaces;

import org.example.model.User;
import java.io.IOException;
import java.util.List;

public interface ExcelService {
    void exportUsers(List<User> listUsers, String filePath) throws IOException;
    List<User> importUsers(String filePath) throws IOException;
}
