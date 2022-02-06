package ru.ibusewinner.fundaily.newsgui.command;

import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ibusewinner.fundaily.newsgui.NewsGUI;
import ru.ibusewinner.fundaily.newsgui.gui.NewsMenu;
import ru.ibusewinner.fundaily.newsgui.items.NewsItem;
import ru.ibusewinner.fundaily.newsgui.items.NewsType;
import ru.ibusewinner.plugin.buseapi.BuseAPI;
import ru.ibusewinner.plugin.buseapi.command.ICommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewsCommand extends ICommand {
    public NewsCommand(JavaPlugin plugin, String name) {
        super(plugin, name);
    }

    @Override
    public void execute(CommandSender commandSender, String s, String[] args) {
        if (args.length == 0) {
            if (commandSender.hasPermission("news.admin")) {
                commandSender.sendMessage(BuseAPI.getMessageManager().convertMessage("&aКоманды: /news <create/publish/delete/text/title/author/type>"));
                commandSender.sendMessage("Всё блин понятно!");
            } else {
                NewsGUI.getGuiapi().openGUI((Player) commandSender, new NewsMenu(NewsGUI.getInstance(), (Player) commandSender));
            }
        } else {
            if (args[0].equalsIgnoreCase("open")) {
                NewsGUI.getGuiapi().openGUI((Player) commandSender, new NewsMenu(NewsGUI.getInstance(), (Player) commandSender));
            } else {
                if (commandSender.hasPermission("news.admin")) {
                    if (args[0].equalsIgnoreCase("create")) {
                        commandSender.sendMessage(BuseAPI.getMessageManager().convertMessage("&aСтатья создана с id: &e"+NewsGUI.getMySQL().createNews()));
                    } else {
                        try {
                            long id = Long.parseLong(args[1]);
                            NewsItem newsItem = NewsGUI.getNewsItemById(id);
                            if (newsItem == null) {
                                commandSender.sendMessage(BuseAPI.getMessageManager().convertMessage("&cСтатья с id "+id+" не существует!"));
                                return;
                            }

                            switch (args[0].toLowerCase()) {
                                case "publish":
                                    newsItem.setPublished(true);
                                    NewsGUI.getMySQL().updateNews(newsItem);
                                    commandSender.sendMessage(BuseAPI.getMessageManager().convertMessage("&aСтатья №"+id+" опубликована!"));
                                    break;
                                case "delete":
                                    NewsGUI.getMySQL().deleteNew(id);
                                    commandSender.sendMessage(BuseAPI.getMessageManager().convertMessage("&cСтатья №"+id+" удалена!"));
                                    break;
                                case "text":
                                    StringBuilder sb = new StringBuilder();
                                    for (int i = 2; i < args.length; i++) {
                                        sb.append(args[i]).append(" ");
                                    }
                                    String text = sb.toString();
                                    List<String> textLore = new ArrayList<>(Arrays.asList(text.split("\\|\\|")));
                                    newsItem.setTextLore(textLore);
                                    NewsGUI.getMySQL().updateNews(newsItem);
                                    commandSender.sendMessage(BuseAPI.getMessageManager().convertMessage("&aТекст обновлён!"));
                                    break;
                                case "title":
                                    sb = new StringBuilder();
                                    for (int i = 2; i < args.length; i++) {
                                        sb.append(args[i]).append(" ");
                                    }
                                    String title = sb.toString();
                                    newsItem.setTitle(title);
                                    NewsGUI.getMySQL().updateNews(newsItem);
                                    commandSender.sendMessage(BuseAPI.getMessageManager().convertMessage("&aЗаголовок обновлён!"));
                                    break;
                                case "author":
                                    sb = new StringBuilder();
                                    for (int i = 2; i < args.length; i++) {
                                        sb.append(args[i]).append(" ");
                                    }
                                    String author = sb.toString();
                                    newsItem.setAuthor(author);
                                    NewsGUI.getMySQL().updateNews(newsItem);
                                    commandSender.sendMessage(BuseAPI.getMessageManager().convertMessage("&aАвтор обновлён!"));
                                    break;
                                case "type":
                                    NewsType newsType = NewsGUI.getNewsTypeById(args[2]);
                                    if (newsType == null) {
                                        commandSender.sendMessage(BuseAPI.getMessageManager().convertMessage("&cТип новостей '"+args[2]+"' не найден!"));
                                        return;
                                    }
                                    newsItem.setNewsType(newsType);
                                    NewsGUI.getMySQL().updateNews(newsItem);
                                    commandSender.sendMessage(BuseAPI.getMessageManager().convertMessage("&aТип новости обновлён!"));
                                    break;
                                case "date":
                                    try {
                                        long parse = Long.parseLong(args[2]);
                                        newsItem.setDate(parse);
                                        commandSender.sendMessage(BuseAPI.getMessageManager().convertMessage("&aВы изменили дату новости!"));
                                    } catch (NumberFormatException e) {
                                        newsItem.setDate(System.currentTimeMillis());
                                        commandSender.sendMessage(BuseAPI.getMessageManager().convertMessage("&aДата новости установлена на текущую!"));
                                    }
                                    NewsGUI.getMySQL().updateNews(newsItem);
                                    break;
                                default:
                                    commandSender.sendMessage(BuseAPI.getMessageManager().convertMessage("&cНеизвестная команда!"));
                                    break;
                            }
                        } catch (NumberFormatException e) {
                            commandSender.sendMessage(BuseAPI.getMessageManager().convertMessage("&cВ аргументе принимается только целое число - айди записи!"));
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<String> complete(CommandSender commandSender, String[] args) {
        if (commandSender.hasPermission("news.admin")) {
            if (args.length == 1) {
                return Lists.newArrayList("create", "publish", "delete", "text", "title", "author", "type", "date");
            } else if (args.length == 2) {
                return Lists.newArrayList("Айди записи");
            }
        }
        return Lists.newArrayList();
    }
}
