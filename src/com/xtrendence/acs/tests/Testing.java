package com.xtrendence.acs.tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xtrendence.acs.accounts.Account;
import com.xtrendence.acs.data.Cart;
import com.xtrendence.acs.data.Item;
import com.xtrendence.acs.data.Repository;
import com.xtrendence.acs.data.Stock;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

import static org.junit.Assert.*;

public class Testing {
    // Depending on the OS, the file separator can be different (usually either / or \).
    private static String fs = System.getProperty("file.separator");

    // A different folder is used to test the application's CRUD functionality.
    public String testFolder = Repository.resourcesFolder + "tests" + fs;

    public String testAccountsFile = testFolder + "accounts.json";
    public String testStockFile = testFolder + "stock.json";

    public String testTextFile = testFolder + "test.txt";

    // Ensuring that the test conditions are always the same at the start.
    public String defaultStock = "{\"1\": {\"code\": \"pc00001\", \"name\": \"Milk\", \"price\": \"1.20\", \"quantity\": \"5\"}, \"2\": {\"code\": \"pc00002\", \"name\": \"Bread\", \"price\": \"1.10\", \"quantity\": \"10\"}, \"3\": {\"code\": \"pc00003\", \"name\": \"Eggs\", \"price\": \"0.70\", \"quantity\": \"3\"}, \"4\": {\"code\": \"pc00004\", \"name\": \"Beef\", \"price\": \"3.00\", \"quantity\": \"4\"}, \"5\": {\"code\": \"pc00005\", \"name\": \"Chicken\", \"price\": \"2.50\", \"quantity\": \"9\"}, \"6\": {\"code\": \"pc00006\", \"name\": \"Bacon\", \"price\": \"1.50\", \"quantity\": \"12\"}, \"7\": {\"code\": \"pc00007\", \"name\": \"Bananas\", \"price\": \"1.25\", \"quantity\": \"3\"}, \"8\": {\"code\": \"pc00008\", \"name\": \"Apples\", \"price\": \"1.35\", \"quantity\": \"2\"}, \"9\": {\"code\": \"pc00009\", \"name\": \"Peaches\", \"price\": \"1.20\", \"quantity\": \"1\"}, \"10\": {\"code\": \"pc00010\", \"name\": \"Chocolate\", \"price\": \"3.25\", \"quantity\": \"2\"}, \"11\": {\"code\": \"pc00011\", \"name\": \"Strawberries\", \"price\": \"2.75\", \"quantity\": \"4\"}, \"12\": {\"code\": \"pc00012\", \"name\": \"Chips\", \"price\": \"1.75\", \"quantity\": \"5\"}, \"13\": {\"code\": \"pc00013\", \"name\": \"Olives\", \"price\": \"0.85\", \"quantity\": \"6\"}, \"14\": {\"code\": \"pc00014\", \"name\": \"Oranges\", \"price\": \"0.50\", \"quantity\": \"8\"}, \"15\": {\"code\": \"pc00015\", \"name\": \"Red Bull\", \"price\": \"1.25\", \"quantity\": \"9\"}, \"16\": {\"code\": \"pc00016\", \"name\": \"Salmon\", \"price\": \"4.00\", \"quantity\": \"5\"}, \"17\": {\"code\": \"pc00017\", \"name\": \"Dignity\", \"price\": \"0.01\", \"quantity\": \"1\"}, \"18\": {\"code\": \"pc00018\", \"name\": \"Coffee\", \"price\": \"2.00\", \"quantity\": \"3\"}, \"19\": {\"code\": \"pc00019\", \"name\": \"Tea\", \"price\": \"1.50\", \"quantity\": \"7\"}, \"20\": {\"code\": \"pc00020\", \"name\": \"Nando's Sauce\", \"price\": \"3.00\", \"quantity\": \"99\"}}";

    public Map<String, String> defaultAccounts = new HashMap<>();

    public Testing() {
        defaultAccounts.put("Temp", "$2a$12$eRCYLwKDTEKjVg8PzNdrs.1BjUBTqkt3uDErPQ25xvFO5Z.bUlv2a");
        defaultAccounts.put("Xtrendence", "$2a$12$dSD3oMqS6vDCSTJ..KSPJunABiXda6z1fpHDswt228p74XKTw4W02");
        defaultAccounts.put("James", "$2a$12$8NVY5i/.tKE6ufjR7MImROLevix0A6sZAqdQHJFtE1LuBrkGQm6LS");
        defaultAccounts.put("Hannibal", "$2a$12$wH6FGLmvKJ.ao98RfEo9XudGtVcvMKEWfNAVPIZ7MxPNXcm94RV.2");
    }

    public void generateTestFiles() {
        File resources = new File(testFolder);
        File stock = new File(testStockFile);
        File accounts = new File(testAccountsFile);
        File test = new File(testTextFile);

        resources.mkdir();

        // Start the test without the files already existing.
        stock.delete();
        accounts.delete();
        test.delete();

        if(!stock.exists()) {
            try {
                stock.createNewFile();

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Map map = gson.fromJson(defaultStock, Map.class);
                String json = gson.toJson(map);

                FileWriter writer = new FileWriter(stock, false);
                writer.write(json);
                writer.close();

                // Ensure that the stock file has been created.
                assertTrue("Test stock file should be created.", stock.exists());
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        if(!accounts.exists()) {
            try {
                accounts.createNewFile();

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(defaultAccounts);

                FileWriter writer = new FileWriter(accounts, false);
                writer.write(json);
                writer.close();

                assertTrue("Test accounts file should be created.", accounts.exists());
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        if(!test.exists()) {
            try {
                test.createNewFile();

                FileWriter writer = new FileWriter(test, false);
                writer.write("default");
                writer.close();

                assertTrue("Test text file should be created.", test.exists());
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }

    // Account is tested separately due to bcrypt hash comparison time.
    public void testAll() {
        testRepository();
        testCart();
        testStock();
    }

    public void testRepository() {
        System.out.println("Testing Repository.create(), Repository.read(), Repository.update(), and Repository.delete().");
        generateTestFiles();
        testCreate();
        testRead();
        testUpdate();
        testDelete();
        System.out.println("Finished testing Repository.");
    }

    public void testCreate() {
        System.out.println("Testing Repository.create().");

        File stock = new File(Repository.stockFile);
        File accounts = new File(Repository.accountsFile);

        stock.delete();
        accounts.delete();

        assertFalse("Stock file should be deleted.", stock.exists());
        assertFalse("Accounts file should be deleted.", accounts.exists());

        Repository.create();

        // To test that the stock.json file is created, and that the expected data is written to it.
        try {
            StringBuilder builder = new StringBuilder();
            FileReader reader = new FileReader(stock);
            Scanner scanner = new Scanner(reader);
            while(scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
            scanner.close();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Map defaultStockMap = gson.fromJson(defaultStock, Map.class);
            String defaultStockJSON = gson.toJson(defaultStockMap);

            Map createdStockMap = gson.fromJson(builder.toString(), Map.class);
            String createdStockJSON = gson.toJson(createdStockMap);

            assertEquals("The stock file should be created with the expected data.", defaultStockJSON, createdStockJSON);
        } catch(Exception e) {
            System.out.println(e);
        }

        // To test that the accounts.json file is created, and that the expected data is written to it.
        try {
            StringBuilder builder = new StringBuilder();
            FileReader reader = new FileReader(accounts);
            Scanner scanner = new Scanner(reader);
            while(scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
            scanner.close();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String defaultAccountsJSON = gson.toJson(defaultAccounts);

            Map createdAccountsMap = gson.fromJson(builder.toString(), Map.class);
            String createdAccountsJSON = gson.toJson(createdAccountsMap);

            assertEquals("The accounts file should be created with the expected data.", defaultAccountsJSON, createdAccountsJSON);
        } catch(Exception e) {
            System.out.println(e);
        }

        System.out.println("Finished testing Repository.create().");
    }

    public void testRead() {
        System.out.println("Testing Repository.read().");

        generateTestFiles();

        String content = Repository.read(testTextFile);

        assertEquals("The content of the text file should be \"default\".", "default", content);

        System.out.println("Finished testing Repository.read().");
    }

    public void testUpdate() {
        System.out.println("Testing Repository.update()");

        generateTestFiles();

        Repository.update(testTextFile, "updated");

        String content = Repository.read(testTextFile);

        assertEquals("The content of the text file should be \"updated\".", "updated", content);

        System.out.println("Finished testing Repository.update().");
    }

    public void testDelete() {
        System.out.println("Testing Repository.delete().");

        generateTestFiles();

        Repository.delete(testTextFile);

        assertFalse("The text file should not exist.", new File(testTextFile).exists());

        System.out.println("Finished testing Repository.delete().");
    }

    public void testCart() {
        System.out.println("Testing Cart.addToCart(), Cart.removeFromCart(), Cart.getQuantity(), and Cart.emptyCart().");

        Map<String, Integer> expectedCart = new HashMap<>();
        expectedCart.put("pc00001", 2);
        expectedCart.put("pc00002", 4);
        expectedCart.put("pc00003", 3);

        Cart cart = new Cart();

        cart.addToCart("pc00001", (float) 1.20);
        cart.addToCart("pc00001", (float) 1.20);

        cart.addToCart("pc00002", (float) 1.10);
        cart.addToCart("pc00002", (float) 1.10);
        cart.addToCart("pc00002", (float) 1.10);
        cart.addToCart("pc00002", (float) 1.10);

        cart.addToCart("pc00003", (float) 0.70);
        cart.addToCart("pc00003", (float) 0.70);
        cart.addToCart("pc00003", (float) 0.70);

        assertEquals("There should be a key of pc00001 with a value of 2, pc00002 with a value of 4, and pc00003 with a value of 3.", expectedCart, cart.getCart());

        expectedCart.put("pc00002", 2);

        cart.removeFromCart("pc00002", (float) 1.10);
        cart.removeFromCart("pc00002", (float) 1.10);

        assertEquals("The should be a key of pc00001 with a value of 2, pc00002 with a value of 2, and pc00003 with a value of 3.", expectedCart, cart.getCart());

        assertEquals("The key of pc00002 should have a value of 2.", 2, cart.getQuantity("pc00002"));

        expectedCart.clear();

        cart.emptyCart();

        assertEquals("The cart should be empty.", expectedCart, cart.getCart());

        System.out.println("Finished testing Cart.");
    }

    public void testStock() {
        System.out.println("Testing Stock.getStock(), Stock.getItem(), Stock.setStock(), and Stock.setItem().");

        generateTestFiles();

        String currentStockFile = Repository.stockFile;

        Repository.stockFile = testStockFile;

        Stock.getStock();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        TreeMap<Integer, TreeMap<String, String>> actualStockMap = new TreeMap<>();

        for(int i = 0; i < Stock.items.size(); i++) {
            Item item = Stock.items.get(i);

            int id = i + 1;
            String code = item.getCode();
            String name = item.getName();
            String price = String.format("%.2f", item.getPrice());
            String quantity = String.valueOf(item.getQuantity());

            TreeMap<String, String> properties = new TreeMap<>();

            properties.put("code", code);
            properties.put("name", name);
            properties.put("price", price);
            properties.put("quantity", quantity);

            actualStockMap.put(id, properties);
        }

        String actualStockJSON = gson.toJson(actualStockMap);

        Map defaultStockMap = gson.fromJson(defaultStock, Map.class);
        String defaultStockJSON = gson.toJson(defaultStockMap);

        assertEquals("The newly fetched stock items should be the same as the default ones.", defaultStockJSON, actualStockJSON);

        String name = Stock.getItem("pc00001").getName();

        assertEquals("The first item by default should be milk.", "Milk", name);

        List<Item> updatedItems = new ArrayList<>();

        Item cherries = new Item("pc00060", "Cherries", (float) 0.80, 8);
        Item noodles = new Item("pc00061", "Noodles", (float) 0.70, 4);
        Item cake = new Item("pc00062", "Cake", (float) 2.00, 3);

        updatedItems.add(cherries);
        updatedItems.add(noodles);
        updatedItems.add(cake);

        Stock.setStock(updatedItems);

        Stock.getStock();

        String cherriesName = Stock.getItem("pc00060").getName();
        String noodlesName = Stock.getItem("pc00061").getName();
        String cakeName = Stock.getItem("pc00062").getName();

        assertEquals("pc00060 should be cherries.", "Cherries", cherriesName);
        assertEquals("pc00061 should be noodles.", "Noodles", noodlesName);
        assertEquals("pc00062 should be cake.", "Cake", cakeName);

        Item butter = new Item("pc00061", "Butter", (float) 1.20, 5);

        Stock.setItem(butter);

        String butterName = Stock.getItem("pc00061").getName();

        assertEquals("pc00061 should now be butter.", "Butter", butterName);

        Repository.stockFile = currentStockFile;

        Stock.getStock();

        System.out.println("Finished testing Stock.");
    }

    public void testAccount() {
        System.out.println("Testing Account.login() and Account.logout().");

        generateTestFiles();

        String currentAccountsFile = Repository.accountsFile;

        Repository.accountsFile = testAccountsFile;

        assertTrue("The test accounts file should exist.", new File(testAccountsFile).exists());

        String username = "Temp";
        String password = "t3mp";

        Account account = new Account(username, password);

        boolean afterLogin = account.getState().loggedIn();

        account.logout();

        boolean afterLogout = account.getState().loggedIn();

        assertTrue("The username and password should be valid, and the login should be successful.", afterLogin);
        assertFalse("The account should be logged out.", afterLogout);

        Repository.accountsFile = currentAccountsFile;

        System.out.println("Finished testing Account.");
    }
}
