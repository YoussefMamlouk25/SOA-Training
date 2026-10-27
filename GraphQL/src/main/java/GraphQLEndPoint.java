import business.ModuleBusiness;
import business.UniteEnseignementBusiness;
import com.coxautodev.graphql.tools.SchemaParser;
import entities.Module;
import graphql.MutationResolver;
import graphql.QueryResolver;
import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndPoint extends SimpleGraphQLServlet {

    public GraphQLEndPoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
        UniteEnseignementBusiness ueBusiness = new UniteEnseignementBusiness();
        ModuleBusiness moduleBusiness = new ModuleBusiness();

        return SchemaParser.newParser()
                .file("schema.graphql")
                .dictionary("TypeModule", Module.TypeModule.class)
                .resolvers(
                        new QueryResolver(ueBusiness, moduleBusiness),
                        new MutationResolver(ueBusiness, moduleBusiness)
                )
                .build()
                .makeExecutableSchema();
    }
}
