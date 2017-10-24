package pl.marian.training.elasticsearch.springdata.repository;

/*
    1)  Create new index es-workshop-4 and type products
    2)  Add missing implementation to Product DTO and make this interface a repository for products
        Each product should have: numeric product code, name, country code and weight in gram
    3)  Add a method to find
        a)  single product by name
        b)  single product by name and country code
        c)  products by weight >=
        d)  Add method to search for products with description matching given parameter
     4) Fill missing implementation in ProductsRepositoryTest (See TODOs)
*/
//TODO: Make this interface elastic search repository
public interface ProductsRepository {
    //TODO: add missing methods
}
