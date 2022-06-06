package com.springbootbatch.demo.config;

import com.springbootbatch.demo.entity.CutomerEntity;
import com.springbootbatch.demo.repository.CustomerRepo;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {
    //to create step and job this two factory will help us
   private JobBuilderFactory jobBuilderFactory;

   private StepBuilderFactory stepBuilderFactory;
//inject repo
private CustomerRepo customerRepo;


// Create Read object
@Bean
    public FlatFileItemReader<CutomerEntity> reader(){
        FlatFileItemReader<CutomerEntity> itemReader=new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/customer.csv"));
        itemReader.setName("KAJAL");
        itemReader.setLinesToSkip(1);
itemReader.setLineMapper(lineMapper());
return itemReader;

    }
//ob of this line mapper is how to read file and map to object
    @Bean
    public LineMapper<CutomerEntity> lineMapper() {
        DefaultLineMapper<CutomerEntity> lineMapper=new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer=new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("id", "name", "time", "duration");

        BeanWrapperFieldSetMapper<CutomerEntity> fielMapper=new BeanWrapperFieldSetMapper<>();
        fielMapper.setTargetType(CutomerEntity.class);

        lineMapper.setLineTokenizer(delimitedLineTokenizer);
        lineMapper.setFieldSetMapper(fielMapper);
        return lineMapper;
}
//pROCESSOR
    @Bean
    public SpringProcessor processor(){


    return new SpringProcessor();
    }

//Writer
    @Bean
    public RepositoryItemWriter<CutomerEntity> writer(){

        RepositoryItemWriter<CutomerEntity>  writer=new RepositoryItemWriter<>();

        writer.setRepository(customerRepo);
        writer.setMethodName("save");
        return writer;
    }
//step
    @Bean
public Step step1(){

    return stepBuilderFactory.get("csv-step").<CutomerEntity,CutomerEntity>chunk(10)
    .reader(reader())
     .processor(processor())
    .writer(writer())
            .taskExecutor(taskExecutor())
            .build();
    }

//job
    @Bean
    public Job runJob(){

    return jobBuilderFactory.get("import")
            .flow(step1()).end().build();

    }

//for asynchronus
    @Bean
    public TaskExecutor taskExecutor(){
        SimpleAsyncTaskExecutor asynchro=new SimpleAsyncTaskExecutor();
        asynchro.setConcurrencyLimit(10);
        return taskExecutor();

    }


}
