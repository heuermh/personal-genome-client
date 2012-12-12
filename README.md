personal-genome-client
======================

Java client for the 23andMe Personal Genome API.

[![Build Status](https://travis-ci.org/heuermh/personal-genome-client.png)](https://travis-ci.org/heuermh/personal-genome-client)


###Hacking personal-genome-client

Install

 * JDK 1.6 or later, http://openjdk.java.net/
 * Apache Maven 3.0.4 or later, http://maven.apache.org/


To build

    $ mvn install


To build demo webapp

    $ cd webapp
    $ mvn assembly:assembly


To run demo webapp, your 23andMe Personal Genome API client details must be provided as command line system properties

    $ java -DapiKey=apiKey \
           -DapiSecret=apiSecret \
           -Dcallback=http://localhost:8080/after-auth-landing/ \
           -Dscope=scope \
           -jar target/personal-genome-webapp-1.0-SNAPSHOT-jar-with-dependencies.jar 


Open

    http://localhost:8080/

in a browser.


###Using personal-genome-client

    OAuthService service = ...;
    Token accessToken = ...;
    JsonFactory = new JsonFactory();
    PersonalGenomeClient client = new ScribePersonalGenomeClient(accessToken, service, jsonFactory);
    
    User user = client.user();
    System.out.println("user id: " + user.getId());
    for (Profile profile : user.getProfiles()) {
      System.out.println("   profile id: " + profile.getId());
    }
    
    UserName userName = client.names();
    System.out.println("user name: " + userName.getFirstName() + " " + userName.getLastName());
    for (ProfileName profileName : userName.getProfilesNames()) {
      System.out.println("   profile name: " + profileName.getFirstName() + " " + profileName.getLastName());
    }
    
    List<Haplogroup> haplogroups = client.haplogroups();
    System.out.println(haplogroups.size() + " haplogroups:");
    for (Haplogroup haplogroup : haplogroups) {
      System.out.println("   profile id: " + haplogroup.getProfileId());
      System.out.println("   paternal: " + haplogroup.getPaternal());
      System.out.println("   maternal: " + haplogroup.getMaternal());
      List<PaternalTerminalSnp> paternalTerminalSnps = haplogroup.getPaternalTerminalSnps();
      if (paternalTerminalSnps != null) {
        System.out.println("   " + paternalTerminalSnps.size() + " paternal terminal SNPs:");
        for (PaternalTerminalSnp paternalTerminalSnp : paternalTerminalSnps) {
          System.out.println("      rsid: " + paternalTerminalSnp.getRsid());
          System.out.println("      ISOGG SNP: " + paternalTerminalSnp.getSnp());
        }
      }
      List<MaternalTerminalSnp> maternalTerminalSnps = haplogroup.getMaternalTerminalSnps();
      System.out.println("   " + maternalTerminalSnps.size() + " maternal terminal SNPs:");
      for (MaternalTerminalSnp maternalTerminalSnp : maternalTerminalSnps) {
        System.out.println("      rsid: " + maternalTerminalSnp.getRsid());
        System.out.println("      rCRS position: " + maternalTerminalSnp.getRcrsPosition());
      }
    }
    
    List<Genotype> genotypes = client.genotypes("rs1234", "rs2345");
    System.out.println(genotypes.size() + " genotypes:");
    for (Genotype genotype : genotypes) {
      System.out.println("   profile id: " + genotype.getProfileId());
      System.out.println("   " + genotype.getValues().size() + " locations:");
      for (Map.Entry<String, String> entry : genotype.getValues().entrySet()) {
        System.out.println("      " + entry.getKey() + ": " + entry.getValue());
      }
    }

    for (Profile profile : user.getProfiles()) {
      Genome genome = client.genome(profile.getId());
      System.out.println("   profile id: " + genome.getProfileId());
      int length = genomes.getValues().length();
      System.out.println("   values: " + (length < 32) ? genomes.getValues() : genomes.getValues().substring(0, 32) + "...")
    }
