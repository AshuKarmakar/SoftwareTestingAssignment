const { Builder, By, Key, util } = require('selenium-webdriver');
require('dotenv').config();


async function example() {
    let driver = await new Builder().forBrowser('chrome').build();
    await driver.get('https://www.google.com');
    await driver.findElement(By.name('q')).sendKeys('Selenium', Key.RETURN);

    driver.close();

}

// example();