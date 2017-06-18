package acropollis.municipali;

import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import acropollis.municipali.binders.MenuBinder;
import acropollis.municipali.bootstrap.view.MunicipaliRowView;
import acropollis.municipali.bootstrap_adapter.ArticleBootstrapAdapter;
import acropollis.municipali.data.article.TranslatedArticle;
import acropollis.municipali.service.ArticlesService;

@EActivity(R.layout.activity_article_information)
public class ArticleInformationActivity extends BaseActivity {
    @ViewById(R.id.root)
    DrawerLayout rootView;

    @ViewById(R.id.article_info)
    MunicipaliRowView articleInfoView;
    @ViewById(R.id.article_category_1)
    TextView articleCategory1View;
    @ViewById(R.id.article_information)
    TextView articleInformationView;

    @Extra("articleId")
    Long articleId;

    @Bean
    ArticlesService articlesService;

    @Bean
    ArticleBootstrapAdapter articleBootstrapAdapter;

    @Bean
    MenuBinder menuBinder;

    private TranslatedArticle article;

    @AfterViews
    void init() {
        article = articlesService.getArticle(articleId);

        articleInfoView.bind(articleBootstrapAdapter.getArticleRowInfo(article, false));
        articleCategory1View.setText(article.getCategories().get(0));
        articleInformationView.setText(getInformation(article.getCategories().get(0)));
    }

    @Click(R.id.back_button)
    void onBackButtonClick() {
        finish();
    }

    @Click(R.id.history)
    void onHistoryClick() {
        Map<String, Serializable> extras = new HashMap<>(); {
            extras.put("articleId", articleId);
        }

        redirect(ArticleHistoryActivity_.class, 0, 0, false, extras);
    }

    private String getInformation(String name) {
        if (name.equals("Donald Trump")) {
            return "Donald John Trump (born June 14, 1946) is the 45th and current President of the United States. Prior to entering politics he was a businessman and television personality.\n" +
                    "\n" +
                    "Trump was born and raised in Queens, New York City, and earned an economics degree from the Wharton School of the University of Pennsylvania. He then took charge of The Trump Organization, the real estate and construction firm founded by his paternal grandmother, which he ran for four and a half decades until 2017. During his real estate career, Trump built, renovated, and managed numerous office towers, hotels, casinos, and golf courses. Besides real estate, he started several side ventures and has licensed the use of his name for the branding of various products and properties. He owned the Miss USA and Miss Universe pageants from 1996 to 2015, and he hosted The Apprentice, a reality television series on NBC television, from 2004 to 2015. His calculated net worth as of 2017 is $3.5 billion, making him the 544th richest person in the world.\n" +
                    "\n" +
                    "Trump first publicly expressed interest in running for political office in 1987. He won two Reform Party presidential primaries in 2000, but withdrew his candidacy early on. In June 2015, he launched his campaign for the 2016 presidential election and quickly emerged as the front-runner among 17 candidates in the Republican primaries. His final opponents suspended their campaigns in May 2016, and in July he was formally nominated at the Republican National Convention along with Indiana governor Mike Pence as his running mate. Many of his campaign statements were controversial or false, generating much free media coverage.\n" +
                    "\n" +
                    "Trump won the general election on November 8, 2016, in a surprise victory against Democratic opponent Hillary Clinton. He became the oldest and wealthiest person to assume the presidency, the first without prior military or government service, and the fifth elected with less than a plurality of the national popular vote. His political positions have been described by scholars and commentators as populist, protectionist, and nationalist.";
        } else if (name.equals("Sean Spicer")) {
            return "Sean Michael Spicer (born September 23, 1971)[1][2] is the White House press secretary and former communications director for President Donald Trump.[3]\n" +
                    "\n" +
                    "Spicer was communications director of the Republican National Committee from 2011 to 2017 and its chief strategist from 2015 to 2017.[4]\n" +
                    "\n" +
                    "On December 22, 2016, Spicer was named as Trump's White House press secretary, and two days later, he was also named White House communications director.[5][6] He assumed both positions with Trump's inauguration on January 20, 2017.\n" +
                    "\n" +
                    "As press secretary, Spicer has drawn controversy for making numerous false or controversial statements as well as for displaying combative behavior with the press. In the first such instance, on the day following Trump's inauguration, Spicer repeated the claim that crowds at the ceremony were the largest ever at such an event and that the press had deliberately under-estimated the number of spectators.[7][8][9] Trump aide Kellyanne Conway defended Spicer's action by stating that he had presented what she called \"alternative facts\" regarding the inauguration's attendance numbers.[10]\n" +
                    "\n" +
                    "Spicer originally stated that the Trump inauguration was \"the most watched ever\", but when that statement was debunked, he said that he had been referring not only to live attendees at the ceremony or those watching on TV, but also viewers who had watched the inauguration online.[11] However, no conclusive figures are available for online viewers,[12] so the claim is unverifiable.[13][14]";
        } else if (name.equals("Hillary Clinton")) {
            return "Hillary Diane Rodham Clinton (/ˈhɪləri daɪˈæn ˈrɒdəm ˈklɪntən/; born October 26, 1947) is an American politician who was the 67th United States Secretary of State from 2009 to 2013, U.S. Senator from New York from 2001 to 2009, First Lady of the United States from 1993 to 2001, and the Democratic Party's nominee for President of the United States in the 2016 election.\n" +
                    "\n" +
                    "Born in Chicago, Illinois, and raised in the Chicago suburb of Park Ridge, Clinton graduated from Wellesley College in 1969, and earned a J.D. from Yale Law School in 1973. After serving as a congressional legal counsel, she moved to Arkansas and married Bill Clinton in 1975. In 1977, she co-founded Arkansas Advocates for Children and Families. She was appointed the first female chair of the Legal Services Corporation in 1978 and became the first female partner at Rose Law Firm the following year. As First Lady of Arkansas, she led a task force whose recommendations helped reform Arkansas's public schools.\n" +
                    "\n" +
                    "As First Lady of the United States, Clinton fought for gender equality and healthcare reform. Because her marriage survived the Lewinsky scandal, her role as first lady drew a polarized response from the public. Clinton was elected in 2000 as the first female senator from New York. She was re-elected to the Senate in 2006. Running for president in 2008, she won far more delegates than any previous female candidate, but lost the Democratic nomination to Barack Obama.[1]\n" +
                    "\n" +
                    "As Secretary of State in the Obama administration from 2009 to 2013, Clinton responded to the Arab Spring, during which she advocated the U.S. military intervention in Libya. She helped organize a diplomatic isolation and international sanctions regime against Iran, in an effort to force curtailment of that country's nuclear program; this would eventually lead to the multinational Joint Comprehensive Plan of Action agreement in 2015. Leaving office after Obama's first term, she wrote her fifth book and undertook speaking engagements.\n" +
                    "\n" +
                    "Clinton made a second presidential run in 2016. She received the most votes and primary delegates in the 2016 Democratic primaries, and formally accepted her party's nomination for President of the United States on July 28, 2016 with vice presidential running mate Senator Tim Kaine. She became the first female candidate to be nominated for president by a major U.S. political party. Despite winning a plurality of the national popular vote, Clinton lost the Electoral College and the presidency to her Republican opponent Donald Trump.[2]";
        } else if (name.equals("Barack Obama")) {
            return "Barack Hussein Obama II (US Listeni/bəˈrɑːk huːˈseɪn oʊˈbɑːmə/ bə-rahk hoo-sayn oh-bah-mə;[1][2] born August 4, 1961) is an American politician who served as the 44th President of the United States from 2009 to 2017. He is the first African American to have served as president, as well as the first born outside the contiguous United States. He previously served in the U.S. Senate representing Illinois from 2005 to 2008, and in the Illinois State Senate from 1997 to 2004.\n" +
                    "\n" +
                    "Obama was born in Honolulu, Hawaii, two years after the territory was admitted to the Union as the 50th state. He grew up mostly in Hawaii, but also spent one year of his childhood in Washington State and four years in Indonesia. After graduating from Columbia University in 1983, he worked as a community organizer in Chicago. In 1988 Obama enrolled in Harvard Law School, where he was the first black president of the Harvard Law Review. After graduation, he became a civil rights attorney and professor, teaching constitutional law at the University of Chicago Law School from 1992 to 2004. Obama represented the 13th District for three terms in the Illinois Senate from 1997 to 2004, when he ran for the U.S. Senate. Obama received national attention in 2004, with his unexpected March primary win, his well-received July Democratic National Convention keynote address, and his landslide November election to the Senate. In 2008, Obama was nominated for president, a year after his campaign began, and after a close primary campaign against Hillary Clinton. He was elected over Republican John McCain, and was inaugurated on January 20, 2009. Nine months later, Obama was named the 2009 Nobel Peace Prize laureate.\n" +
                    "\n" +
                    "During his first two years in office, Obama signed many landmark bills. Main reforms were the Patient Protection and Affordable Care Act (often referred to as \"Obamacare\"), the Dodd–Frank Wall Street Reform and Consumer Protection Act, and the Don't Ask, Don't Tell Repeal Act of 2010. The American Recovery and Reinvestment Act of 2009 and Tax Relief, Unemployment Insurance Reauthorization, and Job Creation Act of 2010 served as economic stimulus amidst the Great Recession, but the GOP regained control of the House of Representatives in 2011. After a lengthy debate over the national debt limit, Obama signed the Budget Control and the American Taxpayer Relief Acts. In foreign policy, Obama increased U.S. troop levels in Afghanistan, reduced nuclear weapons with the U.S.-Russian New START treaty, and ended military involvement in the Iraq War. He ordered military involvement in Libya in opposition to Muammar Gaddafi, and the military operation that resulted in the death of Osama bin Laden.";
        } else if (name.equals("Rex Tillerson")) {
            return "Rex Wayne Tillerson (born March 23, 1952) is an American energy executive, civil engineer, and diplomat who is the 69th and current United States Secretary of State, serving since February 1, 2017.[1] Tillerson joined ExxonMobil in 1975 and rose to serve as the chairman and chief executive officer (CEO) of the company from 2006 to 2016.\n" +
                    "\n" +
                    "Tillerson began his career as an engineer and holds a bachelor's degree in civil engineering from the University of Texas at Austin. By 1989 he had become general manager of the Exxon USA central production division. In 1995, he became president of Exxon Yemen Inc. and Esso Exploration and Production Khorat Inc. In 2006, Tillerson was elected chairman and chief executive officer of Exxon, the world's 6th largest company by revenue.[2][3] Tillerson retired from Exxon effective January 1, 2017, and was succeeded by Darren Woods.[4] He is a member of the National Academy of Engineering.[2]\n" +
                    "\n" +
                    "Tillerson is a longtime volunteer with the Boy Scouts of America, and from 2010 to 2012 was their national president, its highest non-executive position. He is a longtime contributor to Republican campaigns, although he did not donate to Donald Trump's presidential campaign. In 2014, Tillerson, who had made business deals on behalf of Exxon with Russian President Vladimir Putin, opposed the sanctions against Russia.[5] He has previously been the director of the joint US-Russian oil company Exxon Neftegas.[6][7]";
        } else if (name.equals("Mike Pence")) {
            return "Michael Richard \"Mike\" Pence (born June 7, 1959) is an American politician, lawyer, and the 48th and current Vice President of the United States. He previously served as the 50th Governor of Indiana from 2013 to 2017.\n" +
                    "\n" +
                    "Born and raised in Columbus, Indiana, Pence graduated from Hanover College and earned a law degree from the Indiana University Robert H. McKinney School of Law before entering private practice. After losing two bids for a U.S. congressional seat in 1988 and 1990, he became a conservative radio and television talk show host from 1994 to 1999. Pence was elected to the United States Congress in 2000 and represented Indiana's 2nd congressional district and Indiana's 6th congressional district in the United States House of Representatives from 2001 to 2013. He served as the chairman of the House Republican Conference from 2009 to 2011.[1] Pence positioned himself as a principled ideologue and supporter of the Tea Party movement, noting he was \"a Christian, a conservative, and a Republican, in that order.\"\n" +
                    "\n" +
                    "Upon becoming Governor of Indiana in January 2013, Pence initiated the largest tax cut in Indiana's history, pushed for more funding for education initiatives, and continued to increase the state's budget surplus. Pence signed bills intended to restrict abortions, including one that prohibited abortions if the reason for the procedure was the fetus's race, gender, or disability.[2] Pence stirred several high-profile controversies, including with his signature of the Religious Freedom Restoration Act, for which he encountered fierce resistance from moderate members of his party, the business community, and LGBT advocates. He later signed an additional bill acting as an amendment intended to protect LGBT people.\n" +
                    "\n" +
                    "On November 8, 2016, Pence was elected as Vice President, after he dropped out of his gubernatorial re-election campaign in July to become the vice presidential running mate for Republican presidential nominee Donald Trump, who went on to win the presidential election.";
        } else {
            return "";
        }
    }
}
