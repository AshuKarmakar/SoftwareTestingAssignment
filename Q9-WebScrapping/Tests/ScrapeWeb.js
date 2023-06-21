const { Builder, By, Key, until } = require("selenium-webdriver");
const Entity = require("../models/entity");
const { mongoConnection, mongoCloseConnection } = require("../DB/mongoConnect");
const mongoose = require('mongoose');

function sleep(ms) {
  return new Promise((resolve) => {
    setTimeout(resolve, ms);
  });
}


(async function () {

  // Create Connection to MongoDB
  await mongoConnection();

  // Set up the webdriver
  let driver = await new Builder().forBrowser("chrome").build();
  // await driver.get("https://guthib.com/");
  await driver.get("https://medium.com/");

  await sleep(1000);

  // Find all links on the page
  let links = await driver.findElements(By.tagName("a"));

  // handle no links scenario
  if(links.length === 0) {
    console.log("No Links Found... Terminating");
    await driver.quit();
    process.exit(1);
  }

  // Choose a random link
  let random_link = links[Math.floor(Math.random() * links.length)];

  // Click on the random link
  await random_link.click();

  // Wait for the page to load
  //   await driver.wait(until.urlContains(random_link.getAttribute('href')), 10000);

  await sleep(5000);
  
  console.log("Loaded random link");

  // Extract specific data from the linked page
  let paraGraphDataSelectors = await driver.findElements(By.css("p")); // Extract paragraph type data
  let linksDataSelectors = await driver.findElements(By.css("a"));  // Extract link type data

  const paragraphData = await Promise.all(
    paraGraphDataSelectors.map(async (paragraph) => {
      return await paragraph.getText();
    })
  );

  const linksData = await Promise.all(
    linksDataSelectors.map(async (links) => {
      return await links.getAttribute('href');
    })
  );


  paragraphData.forEach(async (paragraph) => {
    if(paragraph) {
      const entity = await Entity.create({
        type: "paragraph",
        data: paragraph,
      });
      console.log(entity);
    }
  });

  linksData.forEach(async (link) => {
    if(link) {
      const entity = await Entity.create({
        type: "link",
        data: link,
      });
      console.log(entity);
    }
  });

  



  // Close the webdriver
  await driver.quit();
  sleep(10000);

  process.exit(0);
})();
