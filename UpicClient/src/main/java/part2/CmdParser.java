package part2;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CmdParser {
  public String ip;
  public Integer numSkiers;
  public Integer numThreads;
  public Integer numLifts = 40;
  public Integer numRuns = 10;

  public void buildCmdParser(String[] args){
    Options options = new Options();

    Option ipOption = new Option("ip", true,"ip of the server");
    ipOption.setRequired(true);
    options.addOption(ipOption);

    Option numThreadsOption = new Option("th", true,"maximum number of threads "
        + "to run (numThreads - max 1024)");
    numThreadsOption.setRequired(true);
    options.addOption(numThreadsOption);

    Option numSkiersOption = new Option("s", true,"number of skier to generate lift "
        + "rides for (numSkiers - max 100000)");
    numSkiersOption.setRequired(true);
    options.addOption(numSkiersOption);

    Option numSkierLiftsOption = new Option("l", true,"number of ski lifts "
        + "(numLifts - range 5-60, default 40)");
    numSkierLiftsOption.setOptionalArg(true);
    options.addOption(numSkierLiftsOption);

    Option numLiftsPerSkierOption = new Option("msl", true,"mean numbers of ski "
        + "lifts each skier rides each day (numRuns - default 10, max 20)");
    numLiftsPerSkierOption.setOptionalArg(true);
    options.addOption(numLiftsPerSkierOption);

    CommandLineParser parser = new DefaultParser();
    HelpFormatter helper = new HelpFormatter();

    CommandLine cmd = null;
    try {
      cmd = parser.parse(options, args);

    } catch (ParseException e) {
      System.out.println(e.getMessage());
      helper.printHelp("CLI Instructions", options);
      System.exit(1);
    }

    if (!checkCLI(cmd)) {
      System.out.println("Invalid Input Parameters, Please Start Again!");
      System.exit(1);
    }
  }

  private boolean checkCLI(CommandLine cmd) {
    String curIP = cmd.getOptionValue("ip");
    String curNumThreads = cmd.getOptionValue("th");
    String curNumSkiers = cmd.getOptionValue("s");
    if(cmd.hasOption("l")) {
      String curNumSkierLifts = cmd.getOptionValue("l");
      if(curNumSkierLifts != null || !curNumSkierLifts.isEmpty()) {
        if (!validNumber(curNumSkierLifts, 5, (int)60)) return false;
        numLifts = Integer.parseInt(curNumSkierLifts);
      }
    }

    if(cmd.hasOption("msl")) {
      String curNumLiftsPerSkier = cmd.getOptionValue("msl");
      if(curNumLiftsPerSkier != null || !curNumLiftsPerSkier.isEmpty()) {
        if(!validNumber(curNumLiftsPerSkier, 0, 20)) return false;
      }
      numRuns = Integer.parseInt(curNumLiftsPerSkier);
    }

    if(curIP == null || curIP.isEmpty()) return false;

    if(curNumSkiers == null || curNumSkiers.isEmpty()) return false;
    if(!validNumber(curNumSkiers, 1, (int)1e5)) return false;

    if(curNumThreads == null || curNumThreads.isEmpty()) return false;
    if(!validNumber(curNumThreads, 1, 1024)) return false;

    ip = curIP;
    numThreads = Integer.parseInt(curNumThreads);
    numSkiers = Integer.parseInt(curNumSkiers);

    return true;
  }

  private boolean validNumber(String s, int min, int max) {
    try {
      int n = Integer.parseInt(s);
      if (n < min) {
        System.out.println("Input number is too small");
        return false;
      } else if (n > max) {
        System.out.println("Input number is too large");
        return false;
      }
    } catch (Exception e) {
      System.out.println("Input is not a number");
      return false;
    }
    return true;
  }


}
