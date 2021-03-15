class CryptoHelper {
  int id;
  String name;
  double price;
  double percentageChange_1h;
  String logoUrl;

  CryptoHelper (int id, String cryptoName, double priceValue, double percentageChange_1hValue, String logoUrl) {
    this.id = id;
    this.name = cryptoName;
    this.price = priceValue;
    this.percentageChange_1h = percentageChange_1hValue;
    this.logoUrl = logoUrl;
  }

}